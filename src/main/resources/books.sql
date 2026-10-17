CREATE TABLE books(
    id INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    author VARCHAR(255) NOT NULL,
    isbn VARCHAR(20) UNIQUE NOT NULL,
    total_copies INT NOT NULL ,
    available_copies INT NOT NULL,
    CONSTRAINT check_copies_logic CHECK(
        total_copies>=0
        AND available_copies>=0 
        AND total_copies>=available_copies
    )
);