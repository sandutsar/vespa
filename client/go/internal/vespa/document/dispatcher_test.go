package document

import (
	"sync"
	"testing"
	"time"

	"github.com/stretchr/testify/assert"
)

type mockFeeder struct {
	failAfterNDocs int
	documents      []Document
	mu             sync.Mutex
}

func (f *mockFeeder) failAfterN(docs int) {
	f.mu.Lock()
	defer f.mu.Unlock()
	f.failAfterNDocs = docs
}

func (f *mockFeeder) Send(doc Document) Result {
	f.mu.Lock()
	defer f.mu.Unlock()
	result := Result{Id: doc.Id}
	if f.failAfterNDocs > 0 && len(f.documents) >= f.failAfterNDocs {
		result.Status = StatusVespaFailure
	} else {
		f.documents = append(f.documents, doc)
	}
	if !result.Status.Success() {
		result.Stats.Errors = 1
	}
	return result
}

func TestDispatcher(t *testing.T) {
	feeder := &mockFeeder{}
	clock := &manualClock{tick: time.Second}
	throttler := newThrottler(clock.now)
	breaker := NewCircuitBreaker(time.Second, 0)
	dispatcher := NewDispatcher(feeder, throttler, breaker)
	docs := []Document{
		{Id: mustParseId("id:ns:type::doc1"), Operation: OperationPut, Body: []byte(`{"fields":{"foo": "123"}}`)},
		{Id: mustParseId("id:ns:type::doc2"), Operation: OperationPut, Body: []byte(`{"fields":{"bar": "456"}}`)},
	}
	for _, d := range docs {
		dispatcher.Enqueue(d)
	}
	dispatcher.Close()
	if got, want := len(feeder.documents), 2; got != want {
		t.Errorf("got %d documents, want %d", got, want)
	}
}

func TestDispatcherOrdering(t *testing.T) {
	feeder := &mockFeeder{}
	commonId := mustParseId("id:ns:type::doc1")
	docs := []Document{
		{Id: commonId, Operation: OperationPut},
		{Id: mustParseId("id:ns:type::doc2"), Operation: OperationPut},
		{Id: mustParseId("id:ns:type::doc3"), Operation: OperationPut},
		{Id: mustParseId("id:ns:type::doc4"), Operation: OperationPut},
		{Id: commonId, Operation: OperationUpdate},
		{Id: mustParseId("id:ns:type::doc5"), Operation: OperationPut},
		{Id: mustParseId("id:ns:type::doc6"), Operation: OperationPut},
		{Id: commonId, Operation: OperationRemove},
		{Id: mustParseId("id:ns:type::doc7"), Operation: OperationPut},
		{Id: mustParseId("id:ns:type::doc8"), Operation: OperationPut},
		{Id: mustParseId("id:ns:type::doc9"), Operation: OperationPut},
	}
	clock := &manualClock{tick: time.Second}
	throttler := newThrottler(clock.now)
	breaker := NewCircuitBreaker(time.Second, 0)
	dispatcher := NewDispatcher(feeder, throttler, breaker)
	for _, d := range docs {
		dispatcher.Enqueue(d)
	}
	dispatcher.Close()

	var wantDocs []Document
	for _, d := range docs {
		if d.Id.Equal(commonId) {
			wantDocs = append(wantDocs, d)
		}
	}
	var gotDocs []Document
	for _, d := range feeder.documents {
		if d.Id.Equal(commonId) {
			gotDocs = append(gotDocs, d)
		}
	}
	assert.Equal(t, len(docs), len(feeder.documents))
	assert.Equal(t, wantDocs, gotDocs)
	assert.Equal(t, int64(0), dispatcher.Stats().Errors)
}

func TestDispatcherOrderingWithFailures(t *testing.T) {
	feeder := &mockFeeder{}
	commonId := mustParseId("id:ns:type::doc1")
	docs := []Document{
		{Id: commonId, Operation: OperationPut},
		{Id: commonId, Operation: OperationPut},
		{Id: commonId, Operation: OperationUpdate}, // fails
		{Id: commonId, Operation: OperationRemove}, // fails
	}
	feeder.failAfterN(2)
	clock := &manualClock{tick: time.Second}
	throttler := newThrottler(clock.now)
	breaker := NewCircuitBreaker(time.Second, 0)
	dispatcher := NewDispatcher(feeder, throttler, breaker)
	for _, d := range docs {
		dispatcher.Enqueue(d)
	}
	dispatcher.Close()
	wantDocs := docs[:2]
	assert.Equal(t, wantDocs, feeder.documents)
	assert.Equal(t, int64(2), dispatcher.Stats().Errors)

	// Dispatching more documents for same ID succeed
	feeder.failAfterN(0)
	dispatcher.start()
	dispatcher.Enqueue(Document{Id: commonId, Operation: OperationPut})
	dispatcher.Enqueue(Document{Id: commonId, Operation: OperationRemove})
	dispatcher.Enqueue(Document{Id: mustParseId("id:ns:type::doc2"), Operation: OperationPut})
	dispatcher.Enqueue(Document{Id: mustParseId("id:ns:type::doc3"), Operation: OperationPut})
	dispatcher.Close()
	assert.Equal(t, int64(2), dispatcher.Stats().Errors)
	assert.Equal(t, 6, len(feeder.documents))
}
