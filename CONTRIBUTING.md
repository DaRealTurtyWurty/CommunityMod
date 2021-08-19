# Contributing Guidelines

## General Pull Request Rules

In order for your PR to be accepted:
* Your code/addition must be SFW (safe for work)
* Your code must not break the mod
* Your code must not change the concept of someone else's idea
* Your code must follow the style guidelines below

## Style Guidelines

1. The order of members in a class **must** be as follows:
- Static Fields
- Static Initializers
- Non-Static Fields
- Non-Static Initializers
- Constructors
- Static Methods
- Non-Static Methods
- Types (Inner Classes)

2. The the order within each of these groups of members **must** be as follows:
- Public
- Protected
- Package
- Private

3. You **must** use spaces in place of tabs and the tab width will be 4 spaces.

4. You **must** follow these braces rules:
- Braces should be on the end of the current line instead of a new line
- Braces do not need to be used if a jump keyword is used and it is the only line after the statement.
- `else`, `catch`, `finally`, `while` and `do...while` loops must go on the same line as the closing brace.

5. The default file encoding **must** be UTF-8.
6. All textures **must** be 16x16(for items and blocks) and a PNG.
7. All sounds **must** be `.ogg`.
8. You **must** add any additions or any vanilla modifications to the README.md.
9. You **must** not use data generators, as this has already caused tonnes of issues for the project.
10. You **must** use Java. **Not** Kotlin, Groovy, Scala or any other Java Bytecode based language.

11. You **must** follow the Oracle Java Naming Conventions which are as follows:
- Packages: lower_snake_case
- Classes: UpperCamelCase (Avoid abbreviations unless the acronym or abbreviation is more widely used than the long form. For example HTML or URI/URL/URN)
- Interfaces: UpperCamelCase (Should describe what the interfaces does, do not prefix with I)
- Methods: camelCase (Should be verbs and well describe what the method does)
- Constants: UPPER_SNAKE_CASE - Sometimes known as SCREAMING_SNAKE_CASE (ANSI Constants should be avoided)
- Variables: camelCase (Should not start with `\_` or `$` and should not be a singular letter unless the letter makes sense. For example `int x = getPosX()`. An example of a violation would be `int j = getFooBar() * WIBBLE_WOBBLE`)

12. Java Identifiers **must** follow the following order:
- public/private/protected
- abstract
- static
- final
- transient
- volatile
- default
- synchronized
- native
- strictfp

13. Imports **must** be formatted as follows:
- Sorted:
  - Non-Static Imports
  - Static Imports
  - Package Origin according to the following order:
    - `java` packages
    - `javax` packages
    - external packages (e.g. `org.xml`)
    - internal packages (e.g. `com.sun`)
- Imports should not be line-wrapped, no matter the length.
- No unused imports should be present.
- Wildcard imports should be avoided unless a very large number of classes(dozens) are imported from that package.
- No more than 1 wildcard import per file should exist.

14. Case lines **must** be indented with a (4 space) tab.
15. Square brackets for arrays **must** be at the type and not at the variable.
16. Annotations **must** be on a separate line from the declaration unless it is annotating a parameter.
17. Lambda Expressions **must** not contain a body if the body is only 1 line.
18. Method References **must** be used in replacement for a lambda expression when possible.
19. Parameter Types in lambda expressions **must** be ommited unless they increase readability.
20. Redudent Parenthesis **must** be removed unless they are clearly increasing readabgility.
21. Long literals **must** use uppercase `L`.
22. Hexadecimal literals **must** use uppercase `A-F`.
23. All other literals **must** use lowercase letters.

For any other niche cases, refer to https://cr.openjdk.java.net/~alundblad/styleguide/index-v6.html
