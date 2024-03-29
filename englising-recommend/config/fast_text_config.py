from gensim.models import KeyedVectors


fast_text_model = KeyedVectors.load_word2vec_format('./word_model/wiki-news-300d-1M-subword.vec')
print("imported model")

