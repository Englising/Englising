from gensim.models import KeyedVectors

model_path = '/app/word_model/wiki-news-300d-1M-subword.vec'
fast_text_model = KeyedVectors.load_word2vec_format(model_path)

print("imported model")

