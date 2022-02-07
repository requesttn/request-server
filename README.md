# Request Server

## Setup a local workspace

This
guide
explains
how
to
set
up
your
environment
for
development
of
the
server.
It
includes
information
about
prerequisites,
configuring
your
IDE,
and
running
server
locally
to
verify
your
setup.

## Prerequisites

### JDK 15

`TODO:`
Include
Java
installation
instructions

### Git

Installing
Git
differes
from
one
operating
system
to
another

-

In
Debian-based
distros,
run `sudo apt-get install git`
.

-

In
windows,
you
can
either

-

download
the
windows [installer](http://git-scm.com/download/win)
and
run
it.

-

or
using [Chocolately](https://chocolatey.org/install)
, **
run
as
administrator** `choco install git.install -y --params "/GitAndUnixToolsOnPath /WindowsTerminal`
.

### IDE

We
suggest [IntelliJ IDEA](https://www.jetbrains.com/idea/)

### Mysql

We
suggest [Wamp Server]()
for
windows

### Environnement Variables

#### Database Server

- `MYSQL_USER`:
  user's
  username
  in
  mysql.
- `MYSQL_PASSWORD`:
  user's
  password
  in
  mysql.

#### Mail Server

- `GMAIL_USER:`
  company
  or
  developer's
  email
- `GMAIL_PASSWORD:`
  company
  or
  developer's
  App
  password

> :warning: **App password** is not your regular gmail password, follow this [link](https://support.google.com/accounts/answer/185833?hl=en) to create one.

## Get the code

## Configure your IDE

### Configuration of IntelliJ IDEA

These
steps
are
very
important.
They
allow
you
to
focus
on
the
content
and
ensure
that
the
code
formatting
always
goes
well.
Did
you
know
that [IntelliJ allows for reformatting selected code](https://www.jetbrains.com/help/idea/reformat-and-rearrange-code.html#reformat_code)
if
you
press `Ctrl + Alt + L`
?

`TODO:`
Include
a
small
tutorial
for
IntelliJ
IDEA
configuration
such
as
enabling
annotations,
Setting
up
Checkstyle,
Code
formatting...
