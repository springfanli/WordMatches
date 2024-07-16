import random
import string


def random_word(length: int) -> str:
    letters = string.ascii_lowercase
    return ''.join(random.choice(letters) for i in range(length))


def generate_large_file(file_name: str, size_in_mb: int):
    size_in_bytes = size_in_mb * 1024 * 1024
    with open(file_name, 'w') as f:
        while f.tell() < size_in_bytes:
            word = random_word(random.randint(1, 10))
            f.write(word + ' ')
            if random.random() < 0.2:  # Randomly add new lines
                f.write('\n')


def generate_predefined_words(file_name: str, num_words: int):
    with open(file_name, 'w') as f:
        for _ in range(num_words):
            word = random_word(random.randint(1, 10))
            f.write(word + '\n')


def main():
    print('Please wait, generating large test data...')
    generate_large_file('large_input.txt', 20)
    generate_predefined_words('predefined_10k_words.txt', 10000)
    print('Done!')


if __name__ == '__main__':
    main()
