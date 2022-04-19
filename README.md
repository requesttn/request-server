# Request Server
## Setup a local workspace
This guide explains how to set up your environment for development of the server. It includes information about prerequisites, configuring your IDE, and running server locally to verify your setup.
## Prerequisites
### JDK 15
> *TODO* Include Java installation instructions
### Git
Installing Git differes from one operating system to another
- In Debian-based distros, run `sudo apt-get install git`.
- In Windows, you can either
  - Download the windows [installer](http://git-scm.com/download/win) and run it.
  - Install [Chocolately](https://chocolatey.org/install) and run the command below **as administrator**
 ```
  choco install git.install -y --params "/GitAndUnixToolsOnPath /WindowsTerminal
  ```
### IDE
We recommend [IntelliJ IDEA](https://www.jetbrains.com/idea/)
### Mysql
We recommend [Wamp Server](https://www.wampserver.com/en/) for Windows
### Environnement Variables
Key | Description
--- | ---
`MYSQL_USER` | Your mysql server username
`MYSQL_PASSWORD` | Your mysql server password
`GMAIL_USER` | Your email
`GMAIL_PASSWORD` | Your App password
> :warning: **App password** is not your regular gmail password, follow this [link](https://support.google.com/accounts/answer/185833?hl=en) to create one.

## Get the code
## Configure your IDE
### Configuration of IntelliJ IDEA
These steps are very important. They allow you to focus on the content and ensure that the code formatting always goes well. Did you know that [IntelliJ allows for reformatting selected code](https://www.jetbrains.com/help/idea/reformat-and-rearrange-code.html#reformat_code) if you press `Ctrl + Alt + L`?

> *TODO* Include a small tutorial for IntelliJ IDEA configuration such as enabling annotations, Setting up Checkstyle, Code formatting...
