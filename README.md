# crafting interpreters

## Chapter 2 Challenges

- Open source language of choice: **TypeScript**
    - Scanner Implementation: [typescript/src/compiler/scanner.ts](https://github.com/microsoft/TypeScript/blob/main/src/compiler/scanner.ts)
    - Parser Implementation: [typescript/src/compiler/parser.ts](https://github.com/microsoft/TypeScript/blob/main/src/compiler/parser.ts)
    - The parser and scanner for TS are handwritten.

- Some reasons to not JIT 
    - I think one reason to not use JIT is that since JITs need to know the architecture of the host machine, there's an additional overhead in determining that and that could hamper compiler speed.
    - Another reason is that because JITs compile to host architecture machine code and is not specifically targeted to an architecture, there may be some platform specific optimisation that'll increase performance that a JIT compiler will not be able to do, limiting it's performance and maybe portability