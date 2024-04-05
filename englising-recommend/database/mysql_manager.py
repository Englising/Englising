from sqlmodel import create_engine
from sqlalchemy.orm import sessionmaker
from dotenv import load_dotenv
from contextlib import contextmanager

import os

load_dotenv()

DATABASE_URL = os.getenv("DATABASE_URL")
engine = create_engine(DATABASE_URL, echo=True)

Session = sessionmaker(autocommit=False, autoflush=False, bind=engine)


@contextmanager
def get_session():
    session = Session()
    try:
        yield session
    finally:
        session.close()
