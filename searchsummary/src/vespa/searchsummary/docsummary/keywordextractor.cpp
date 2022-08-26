// Copyright Yahoo. Licensed under the terms of the Apache 2.0 license. See LICENSE in the project root.

#include "keywordextractor.h"
#include "idocsumenvironment.h"
#include <vespa/searchlib/parsequery/stackdumpiterator.h>
#include <vespa/vespalib/stllike/hashtable.hpp>
#include <vespa/vespalib/util/size_literals.h>

/** Tell us what parts of the query we are interested in */

namespace search::docsummary {


bool useful(search::ParseItem::ItemCreator creator)
{
    return creator == search::ParseItem::CREA_ORIG;
}


KeywordExtractor::KeywordExtractor(IDocsumEnvironment * env)
    : _env(env),
      _legalPrefixes(),
      _legalIndexes()
{
}


KeywordExtractor::~KeywordExtractor() = default;

bool
KeywordExtractor::IsLegalIndexName(const char *idxName) const
{
    return _legalIndexes.find(idxName) != _legalIndexes.end();
}

KeywordExtractor::IndexPrefix::IndexPrefix(const char *prefix)
    : _prefix(prefix)
{
}

KeywordExtractor::IndexPrefix::~IndexPrefix() = default;

bool
KeywordExtractor::IndexPrefix::Match(const char *idxName) const
{
    return vespalib::starts_with(idxName, _prefix);
}

void
KeywordExtractor::AddLegalIndexSpec(const char *spec)
{
    if (spec == nullptr)
        return;

    vespalib::string toks(spec); // tokens
    vespalib::string tok; // single token
    size_t           offset; // offset into tokens buffer
    size_t           seppos; // separator position

    offset = 0;
    while ((seppos = toks.find(';', offset)) != vespalib::string::npos) {
        if (seppos == offset) {
            offset++; // don't want empty tokens
        } else {
            tok = toks.substr(offset, seppos - offset);
            offset = seppos + 1;
            if (tok[tok.size() - 1] == '*') {
                tok.resize(tok.size() - 1);
                AddLegalIndexPrefix(tok.c_str());
            } else {
                AddLegalIndexName(tok.c_str());
            }
        }
    }
    if (toks.size() > offset) { // catch last token
        tok = toks.substr(offset);
        if (tok[tok.size() - 1] == '*') {
            tok.resize(tok.size() - 1);
            AddLegalIndexPrefix(tok.c_str());
        } else {
            AddLegalIndexName(tok.c_str());
        }
    }
}


vespalib::string
KeywordExtractor::GetLegalIndexSpec()
{
    vespalib::string spec;

    if (!_legalPrefixes.empty()) {
        for (auto& prefix : _legalPrefixes) {
            if (!spec.empty()) {
                spec.append(';');
            }
            spec.append(prefix.get_prefix());
            spec.append('*');
        }
    }

    for (const auto & index : _legalIndexes) {
        if (!spec.empty())
            spec.append(';');
        spec.append(index);
    }
    return spec;
}


bool
KeywordExtractor::IsLegalIndex(vespalib::stringref idxS) const
{
    vespalib::string resolvedIdxName;

    if (_env != nullptr) {
        resolvedIdxName = _env->lookupIndex(idxS);
    } else {

        if ( ! idxS.empty() ) {
            resolvedIdxName = idxS;
        } else {
            resolvedIdxName = "__defaultindex";
        }
    }

    if (resolvedIdxName.empty())
        return false;

    return (IsLegalIndexPrefix(resolvedIdxName.c_str()) ||
            IsLegalIndexName(resolvedIdxName.c_str()));
}


char *
KeywordExtractor::ExtractKeywords(vespalib::stringref buf) const
{
    search::SimpleQueryStackDumpIterator si(buf);
    char keywordstore[4_Ki]; // Initial storage for keywords buffer
    search::RawBuf keywords(keywordstore, sizeof(keywordstore));

    while (si.next()) {
        search::ParseItem::ItemCreator creator = si.getCreator();
        switch (si.getType()) {
        case search::ParseItem::ITEM_NOT:
            /**
             * @todo Must consider only the first argument on the stack.
             * Difficult without recursion.
             */
            break;

        case search::ParseItem::ITEM_PHRASE:
            {
            // Must take the next arity TERMS and put together
            bool phraseterms_was_added = false;
            int phraseterms = si.getArity();
            for (int i = 0; i < phraseterms; i++) {
                si.next();
                search::ParseItem::ItemType newtype = si.getType();
                if (newtype != search::ParseItem::ITEM_TERM &&
                    newtype != search::ParseItem::ITEM_NUMTERM)
                {
                    // stack syntax error
                    // LOG(debug, "Extracting keywords found a non-term in a phrase");
                    // making a clean escape.
                    keywords.reset();
                    goto iteratorloopend;
                } else {
                    if (!IsLegalIndex(si.getIndexName()))
                        continue;
                    // Found a term
                    vespalib::stringref term = si.getTerm();
                    search::ParseItem::ItemCreator term_creator = si.getCreator();
                    if ( !term.empty() && useful(term_creator)) {
                        // Actual term to add
                        if (phraseterms_was_added) {
                            // Not the first term in the phrase
                            keywords += " ";
                        } else {
                            phraseterms_was_added = true;
                        }

                        keywords.append(term.data(), term.size());
                    }
                }
            }
            if (phraseterms_was_added) {
                // Terms was added, so 0-terminate the string
                keywords.append("\0", 1);
            }

            break;
        }
        case search::ParseItem::ITEM_PREFIXTERM:
        case search::ParseItem::ITEM_SUBSTRINGTERM:
        case search::ParseItem::ITEM_EXACTSTRINGTERM:
        case search::ParseItem::ITEM_NUMTERM:
        case search::ParseItem::ITEM_TERM:
            if (!IsLegalIndex(si.getIndexName()))
                continue;
            {
                // add a new keyword
                vespalib::stringref term = si.getTerm();
                if ( !term.empty() && useful(creator)) {
                    // An actual string to add
                    keywords.append(term.data(), term.size());
                    keywords.append("\0", 1);
                }
            }
            break;

        default:
            // Do nothing to AND, RANK, OR
            break;
        }
    }
 iteratorloopend:
    // Add a 'blank' keyword
    keywords.append("\0", 1);

    // Must now allocate a string and copy the data from the rawbuf
    void *result = malloc(keywords.GetUsedLen());
    if (result != nullptr) {
        memcpy(result, keywords.GetDrainPos(), keywords.GetUsedLen());
    }
    return static_cast<char *>(result);
}

}
