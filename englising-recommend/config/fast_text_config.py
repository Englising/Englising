from gensim.models import KeyedVectors
import os

current_dir = os.path.dirname(os.path.abspath(__file__))
model_path = os.path.join(current_dir, 'english-recommend', 'word_model', 'wiki-news-300d-1M-subword.vec')

fast_text_model = KeyedVectors.load_word2vec_format('./word_model/wiki-news-300d-1M-subword.vec')
print("imported model")

