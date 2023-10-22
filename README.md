Your task is to create two functions, **_encode_** and **_decode_** that will allow you to encrypt and decrypt a phrase using a **'key'**.

The scheme is as follows:

> `phrase` + `key` = `encPhrase`

> `encPhrase` + `key` = `phrase`

**Mechanics of encryption and decryption**
- A **phrase** and a **key** will be given.
- By using these, the encrypted or decrypted phrase will be generated.
- The mechanism consists of determining the ASCII code of each character of the phrase and the key.
- To calculate offsets of each character of the key has to be add to the length of the key ([see example below](#encryption-example)).
- Only ASCII ranges from **65-90** (uppercase) and **97-122** (lowercase) will be considered ([see the picture below](#ascii-table)).
- Once all the ASCII values have been obtained, a run through of the phrase is performed, adding the offset for each character ([see example below](#encryption-example)).
- The phrase and key will be read simultaneously, so that the first character of the phrase uses the first character of the key and so on.

## **ASCII TABLE** :
![ascii.png](https://i.postimg.cc/jS7DfbwR/ascci-Edit.png)

**Important notes**
- If the offset moves the ASCII code out of the set ranges it should start in the next range.

For example:
> If ASCII code + OFFSET > 122, it should start from 65.

> If ASCII code + OFFSET > 90 and < 97, it should start from 97.

- The key length will not exceed 25.

- The decoding procedure is the opposite of the encryption ([see the decryption example below](#encryption-example)).

- If the phrase doesn't exist or is empty return an empty string.

- If the key passed doesn't exist or is empty you'll not be able to encrypt the phrase so returns the phrase.

Possible cases:

> 1. `Phrase length = key length` Nothing should change.

> 2. `Phrase length > key length` Once the end of the key is reached, the key should start back again at the first character ([see the example](#encryption-example)).

> 3. `Phrase length < key length` Once the end of the phrase is reached, no more changes should be done.

## Examples:

### Encryption example:

```java
Legend:
- LD     (Last Digit) 
- OFFSET (Last digit + Key length)
- EC     (Encrypted Char)

key: "fgh" (length: 3)
-------------------------------------
| char  => ASCII => LD =>  OFFSET   |
| 'f'   =>  102  =>  2 => 2 + 3 = 5 |
| 'g'   =>  103  =>  3 => 3 + 3 = 6 |
| 'h'   =>  104  =>  4 => 4 + 3 = 7 |
-------------------------------------

phrase: "deargod"
------------------------------------------
| char => ASCII => ASCII + OFFSET => EC  |
| 'd'  =>  100  => 100 + 5 = 105  => 'i' |
| 'e'  =>  101  => 101 + 6 = 107  => 'k' |
| 'a'  =>   97  =>  97 + 7 = 104  => 'h' |
| 'r'  =>  114  => 114 + 5 = 119  => 'w' |
| 'g'  =>  103  => 103 + 6 = 109  => 'm' |
| 'o'  =>  111  => 111 + 7 = 118  => 'v' |
| 'd'  =>  100  => 100 + 5 = 105  => 'i' |
------------------------------------------

"deargod" + "fgh" = "ikhwmvi"
```
### Decryption example:

```java
key: "fgh" (length: 3)
-------------------------------------
| Char  => ASCII => LD => OFFSET    |
| 'f'   =>  102  =>  2 => 2 + 3 = 5 |
| 'g'   =>  103  =>  3 => 3 + 3 = 6 |
| 'h'   =>  104  =>  4 => 4 + 3 = 7 |
-------------------------------------

encryptedPhrase: "ikhwmvi"
------------------------------------------
| Char => ASCII => ASCII + OFFSET => EC  |
| 'i'  =>  105  => 105 - 5 = 100  => 'd' |
| 'k'  =>  107  => 107 - 6 = 101  => 'e' |
| 'h'  =>  104  => 104 - 7 = 97   => 'a' |
| 'w'  =>  119  => 119 - 5 = 114  => 'r' |
| 'm'  =>  109  => 109 - 6 = 103  => 'g' |
| 'v'  =>  118  => 118 - 7 = 111  => 'o' |
| 'i'  =>  105  => 105 - 5 = 100  => 'd' |
------------------------------------------

"ikhwmvi" + "fgh" = "deargod"
```

**YOU'VE GOT THIS!!!**

Kata developed by **[gonzalorg8799](https://github.com/gonzalorg8799)**, **_efrain.vm_**, **_sergio.gg_** and **[Quathar](https://github.com/Quathar)** from **metrica_sep23**