// Copyright Yahoo. Licensed under the terms of the Apache 2.0 license. See LICENSE in the project root.

#pragma once

#include "entryref.h"
#include "buffer_type.h"
#include <vespa/vespalib/util/array.h>

namespace vespalib::datastore {

class FreeList;

/**
 * Class containing the free list for a single buffer.
 *
 * The free list is a stack of EntryRef's that can be reused.
 */
class BufferFreeList {
private:
    using EntryRefArray = vespalib::Array<EntryRef>;

    std::atomic<ElemCount>& _dead_elems;
    uint32_t _array_size;
    FreeList* _free_list;
    EntryRefArray _free_refs;

    void attach();
    void detach();

public:
    BufferFreeList(std::atomic<ElemCount>& dead_elems);
    ~BufferFreeList();
    void enable(FreeList& free_list);
    void disable();

    void on_active(uint32_t array_size) { _array_size = array_size; }
    bool enabled() const { return _free_list != nullptr; }
    bool empty() const { return _free_refs.empty(); }
    void push_entry(EntryRef ref) {
        if (empty()) {
            attach();
        }
        _free_refs.push_back(ref);
    }
    EntryRef pop_entry() {
        EntryRef ret = _free_refs.back();
        _free_refs.pop_back();
        if (empty()) {
            detach();
        }
        _dead_elems.store(_dead_elems.load(std::memory_order_relaxed) - _array_size, std::memory_order_relaxed);
        return ret;
    }
};

}
