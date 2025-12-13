# Current Status
Just a prototype where only some modules work. Literally only few of the features work as of now.

All the working features (Including proper conversion to JSON) have a ✅ next to them in the [#structure-and-syntax](https://github.com/ReactivePlayZ/RML-Interpreter/tree/main?tab=readme-ov-file#structure-and-syntax) section of this README.
> Note: Multi-line elements are not implemented yet.

# RML
RML (Standing for "`Reactive's/Readable Markup Language`") was initially created to **store information** and **logging of entries**. This is a __interpreter of RML to JSON__. It is a way to represent data in the most human readable way possible, which includes Unicode and a flexible structure to define things.

# Why?
To explain the use case a bit more:

I like to store some personal data (Such as watchlists) in a readable but structured way. That is what I did but sometimes this data can be helpful in other formats (Maybe importing to an online watchlist or converting to a database) so this interpreter serves as a way to automatically convert that data in an efficient and not cluttered way while still maintaining all relevant data.

# Example of Syntax and Conversion
If you just want to see how the syntax of RML and the interpretation to JSON looks like, then here:
```
This is a RML file. This portion is a header
It contains information or descriptions.
Created by @ReactivePlayZ

=== Skills ===
- Language acquisition: @boolean true
- Languages:
- English
- Bangla
- Hindi
- Japanese

I'm plain text. I'll get ignored by the interpreter :(

=== Texts ===
- Paragraph: Lorem ipsum dolor sit amet,
| consectetur adipiscing elit,
| sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.
| Ut enim ad minim veniam...
- Lines: @number 4 // Will update later when more lines are added

=== Grocery List ===
(EN)
- Apples
- Oranges
- Bananas

(JP)
- 苺
- みかん
- もも

- Also get Tomatoes
```
Converted to:
```json
{
    "file_header": [
        "This is a RML file. This portion is a header",
        "It contains information or descriptions.",
        "Created by @ReactivePlayZ"
    ],
    "sections": [
        {
            "section_name": "Skills",
            "elements": [
                {
                    "key": "Language acquisition",
                    "value": true
                },
                {
                    "key": "Languages",
                    "value": {
                        "list": [
                            "English",
                            "Bangla",
                            "Hindi",
                            "Japanese"
                        ]
                    }
                }
            ]
        },
        {
            "section_name": "Texts",
            "elements": [
                {
                    "key": "Paragraph",
                    "value": [
                        "Lorem ipsum dolor sit amet,",
                        "consectetur adipiscing elit,",
                        "sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.",
                        "Ut enim ad minim veniam..."
                    ]
                },
                {
                    "key": "Lines",
                    "value": 4
                }
            ]
        },
        {
            "section_name": "Grocery List",
            "elements": [
                {
                    "section_name": "EN",
                    "elements": [
                        {
                            "list": [
                                "Apples",
                                "Oranges",
                                "Bananas"
                            ]
                        }
                    ]
                },
                {
                    "section_name": "JP",
                    "elements": [
                        {
                            "list": [
                                "苺",
                                "みかん",
                                "もも"
                            ]
                        }
                    ]
                },
                {
                    "list": [
                        "Also get Tomatoes"
                    ]
                }
            ]
        }
    ]
}
```

# Structure and Syntax
## File Header ✅
Anything outside of a section (Which we'll get to in a bit) becomes the file header. This can be any type of information that, typically,  describes what the file is for.

For example:
```
hellooo
im the file header
the interpreter loves me and doesn't ignore me lol

=== Section ===
Plain text like this is valid, but ignored by the interpreter
```

File headers are also optional.
## Sections ✅
Sections are what holds all the important information. They can hold sub sections and elements (key-values or lists). A section can not hold duplicate elements. However, different sections can have the same element.
>
They are denoted by surrounding equals (e.g `=== Section Name ===`) and of course, can be Unicode.
>
Sections don't have a defined ending so the rest of the file continues with the section unless a new one is defined. That means, out of section text can only be before a section is created, hence why that is the file header.  
>
If a section is not used at all in a file, then the file itself is considered the section. In the converter, this would give the `section_name` to be the file's name (or else just `null`). Though, it is recommended to include a section for clear intentions.
## Sub Sections ✅
They are sections within sections that are enclosed in parenthesis (e.g `(Section 1a)`). They work the same way as sections do but can't have sub sections within them.
## Elements ✅
There are three elements to work with: `key-value`, `lists`, and `comments`.

> Note: Comments are considered as notes in RML and plain text (Without starting with any - or //) is considered as the file's comments and isn't read by the interpreter.

### Key Value Pairs ✅
They simply have a key and a value that is stored in this key. Both of these can be Unicode but the only thing separating them is a separater (There are two separaters, a colon `:` and a hyphen `-`). Such as:
```
- Key: Value
- Lorem ipsum: dolor sit amet
- 漢字: かんじ

- Hey look - This key-value is using a different separator
- Am I weird - <- He's concerned
- 日本 - Japan
```

Key Value Pairs can also hold multi-line texts (Similar to lists except the text is continuous and not separate). They can be used by starting the following line of a key-value with a pipe `|`. Example:
```
- Key: Value
| I'm continuing the previous entry's value
| by using the | symbol.
| It's helpful for storing bigger data or in a more organized format.
```
### Lists ✅
Lists are the same as Key Value Pairs except they just don't have the value.
```
- Apples
- Oranges
- Bananas
- みかん
- もも
```

Lists also break when there is an empty line in-between. For example:
```
- Apples
- Oranges
- Bananas

- みかん
- もも
```
There are now two lists, the first one being: `["Apples", "Oranges", "Bananas"]`. And the second one being: `["みかん", "もも"]`. This distinction is important for the interpreter.

### Comments ✅
Comments are denoted by `//` and anything after is considered a comment. These are actually read as notes to sections and elements rather than a file comment. For a comment that is skipped by the interpreter, use plain text (That is, without any `//` or `-`). They can be specific notes or additional information that might be needed.
```
// The previous entry is not the most accurate data
// I'm a comment!
// <some code> Could be used
// コメントですね。
```
This is also a multi-line comment.

Multi-line comments are simply comments followed by another. To break a multi-line comment, use a non-comment line in-between. 
```
// These comments

// are separate
- I'm a list
// And aren't a multi-line comment
```

This is to distinguish for the interpreter, just like lists.

### All three elements used together
```
- Grocery List:
- Apples
- Oranges
- Milk // That dad never came back with
- Onions

// The above empty line breaks the list
// Without it, the list would keep continuing
// Just like sections
- Another List:
- Start Task X
- Complete Tast B
- Remove Task C

- Or just a good ol' key: with a normal value // And comment!
- And some key: That holds info
| And tons of information
| that need to be
| included
```

> Note: Comments in multi-line key values are for the entire key-value pair and become multi-line comments.
> ```
> - Key: Value
> | Following line
> | This line has a comment // hi.
> | The comment isn't so helpful // oh :(
> ```
> Basically, the comment for this entry becomes:
> ```
> hi.
> oh :(
> ```
## Data Types
By default, all data types are of String. RML has 5 data types:
- `Strings` (Any text data) ✅
- `number` (Integers and Floating points)
- `boolean` (true/false)
- `date` (By ISO 8601 Standard, uses YYYY-MM-DD)
- `time` (Uses hh:mm:ss, 24h format)

Specifying types uses the `@` symbol and any of the types (Except for Strings) and then followed by the value to hold (`@dataType x`). For example:
```
- Tasks remaining: @number 5
- Current Task Complete?: @boolean false
- Due Date: @date 2025-11-19 // Read as 19th Nov. 2025
- Due Time: @time 14:30:00 // 2:30 PM
```

`date` and `time` can be used together in one line:
```
- Due Time: @date 2025-11-19 @time 14:30:00 // Read as 19th Nov. 2025 @ 2:30 PM
```

# Conversion to JSON
A single value will be a JSON Object, however, multiple values in a single line will be an Array instead. This is a principle that is followed everywhere (An example can be found in the File Header section)

You'll also see that conversion to JSON can be quite lengthy. Generally, this is fine. But for small data, it can seem a bit complex. It has to remain lengthy in order to preserve exact data from RML.
## File Header
A file header is a JSON Array.
##
```
hi, I'm a file header
```
Gets converted to:
```json
{
    "file_header": ["hi, I'm a file header"]
}
```
##
Since it's an array, a multi-line header would be converted like this:
```
im a file header
file created by someone
very descriptive indeed
```
Gets converted to:
```json
{
    "file_header": [
        "im a file header",
        "file created by someone",
        "very descriptive indeed"
    ]
}
```
## Sections
Sections have a structure like this:
```json
{
    "sections": [
        {
            "section_name": "Name here",
            "elements": []
        }
    ]
}
```
Within the elements array is where all the elements/entries would be placed.
## Sub Sections
They have the same structure as sections, except they are contained within the `elements` array of a section.
```json
{
    "elements": [
        {
            "section_name": "Name goes here",
            "elements": []
        }
    ]
}
```
## Key Values
Since JSON keys aren't recommended to hold spaces or Unicode, what the interpreter will do is create two keys called `key` and `value`.

A key-value pair such as `- 404 Book Vol 4.: Read on June 29th // 10pm` would become:
```json
{
    "key": "404 Book Vol 4.",
    "value": "Read on June 29th",
    "comment": "10pm"
}
```
If the comment was omitted, then the `"comment"` key would also not be created.
> Also, whitespaces at the beginning and end of a key/value is trimmed

Multi-line values also get converted:
```
- Key: Values
| Values // some comment
| Values // another comment
```
to:
```json
{
    "key": "Key",
    "value": [
        "Values",
        "Values"
    ],
    "comment": [
        "some comment",
        "another comment"
    ]
}
```
## Lists
Lists are just regular JSON arrays. For example, here is a list in RML:
```
- Apples
- Oranges
- Bananas
```
It would get converted to:
```json
{
    "list": [
        "Apples",
        "Oranges",
        "Bananas"
    ]
}
```
## Comments
```
// I'm a comment!
```
Gets converted to:
```json
{
    "comment": "I'm a comment!"
}
```
Multi-line comments don't specifically exist in RML but the interpreter can read multiple stand alone comments and group them together.
```
// This comment
// And this one
// We all are connected
// Hopefully, if there are no bugs.
```
Converted to:
```json
{
    "comment": [
        "This comment",
        "And this one",
        "We all are connected",
        "Hopefully, if there are no bugs."
    ]
}
```
Keep in mind that to break the comments in to separate ones, a simple empty line in-between will break them. For example:
```
// This line and
// this is a multi-line comment

// However, this is separate
```
Converted to:
```json
[
    {
        "comment": [
            "This line and",
            "this is a multi-line comment"
        ]
    },
    {
        "comment": "However, this is separate"
    }
]
```
## Data Types
### `number` and `boolean`
They exist in JSON so they can get converted without any issues.
```
- Days till exam: @number 7
- Studied: @boolean false
```
Gets converted to:
```json
[
    {
        "key": "Days till exam",
        "value": 7
    },
    {
        "key": "Studied",
        "value": false
    }
]
```
### `date` and `time`
JSON doesn't have dates and times. So, the converter uses the [ISO 8601 Standard](https://www.iso.org/iso-8601-date-and-time-format.html). Conversion of time also assumes to use UTC.
```
- Date: @date 2025-11-19
- Time: @time 14:30:00
- Date and Time: @date 2025-11-19 @time 14:30:00
```
Converted to:
```json
[
    {
        "key": "Date",
        "value": "2025-11-19"
    },
    {
        "key": "Time",
        "value": "14:30:00Z"
    },
    {
        "key": "Date and Time",
        "value": "2025-11-19T14:30:00Z"
    }
]
```
