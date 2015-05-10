# sim-ephemeral

Helpers for Simulant's process state service

## Background

Agents have access to a temporary in-memory Datomic database during a sim
run (via the process-state service). Certain information (like current auth
credentials) needs to be remembered during a sim run, but is irrelevant to
validation.

## Usage:

1. Add `[io.homegrown/sim-ephemeral "1.0.0"]` to your Simulant project's
   `:dependencies`
2. During sim setup invoke `simulant.sim/create-process-state` after
   `simulant.sim/create-sim`.
3. Inside an agent action, invoke `io.homegrown.sim-ephemeral`'s `retrieve`,
   `store`, or `clear` as necessary. These functions will automatically gain
   access to a process-local, in-memory Datomic database. See individual
   function docs for more information on usage.

## Caveats

Because process-state stores are local to a Simulant process, agents running
on separate processes *will not* be able to access each other's keys/values."

## License

The MIT License (MIT)

Copyright (c) 2015 Homegrown Labs

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.


