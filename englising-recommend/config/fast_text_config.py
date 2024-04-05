import os

from dotenv import load_dotenv
from gensim.models import KeyedVectors

load_dotenv()

model_path = os.getenv('MODEL_PATH')
fast_text_model = KeyedVectors.load_word2vec_format(model_path)

print("imported model")

