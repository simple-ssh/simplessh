# SimpleSSH

**SSH File Manager** — an easy way to manage your VPS/VDS via SSH with a user-friendly interface on Windows, Mac, and Linux.
![Alt text](https://simplessh.com/public/screen/home_banner.jpg)

## About

SimpleSSH is a Java-based application that does **not** require installation on your VPS. It performs the same tasks you would do via command line using PuTTY or Terminal. For example, when you open the file manager to view folders and files, SimpleSSH sends the command:

```bash
sudo ls /var/www/
````

to your VPS via SSH.

The advantage of using SimpleSSH is that you don’t need to memorize all the commands, and it doesn’t consume RAM or disk space on your VPS — which is especially important if your VPS has only 1GB of RAM.

## ✅ Features

- **Tested on Ubuntu Server**
- **Completely Free to Use**
- **No Ads, No Tracking, No Cracking**
- **SSH File Manager** – manage your server files securely via SSH with a user-friendly interface
- **SSH Database Manager** – connect and manage databases remotely
- **Domain & DNS Control** – manage DNS records using BIND9
- **Email Server Management** – setup and manage Postfix, Dovecot, DKIM, DMARC, and SPF
- **No Terminal Knowledge Required** – intuitive design for beginners and pros alike
- **Cross-Platform Interface** – works on Windows, macOS, and Linux
- **Lightweight** – no installation needed on the server itself
- **Amazon EC2 Compatible** – works seamlessly with Ubuntu on EC2
- **Flexible Deployment** – run on your desktop or deploy to a VPS for a permanent URL
- **Secure Backend** – built with Spring Boot (Java 21)
- **Modern Frontend** – developed in React
- **Command-Free File Management** – upload, move, and edit files easily

## 🛠️ Tech Stack

- **Backend:** Java 21 + Spring Boot
- **Frontend:** React
- **Security:** Spring Security,

## How to run simple ssh

https://simplessh.com/info/getting-started/how-to-run-simplessh-on-windows-how-to-run-simplessh-on-mac-how-to-run-simplessh-on-ubuntu-how-to-run-simplessh-on-linux

Perfect — here's a polished version of the **Project Structure & Build Instructions** section formatted specifically for your `README.md` on GitHub:

 
## 🛠️ Project Structure & Build Instructions

### 📁 Folder Structure

- `front2/` – React-based frontend application
- `src/` – Spring Boot backend (requires Java 21)

### ✅ Requirements

- **Java 21**
- **Node.js** (for building the React frontend)
- **Maven** (for building and running the Spring Boot backend)

### 📦 Build Instructions

1. **Build the Frontend**

   Navigate to the `front2/` directory:

   ```bash
   cd front2
   npm install
   npm run build
````
This will generate a production-ready `build/` folder inside `front2/`.


2. **Copy Frontend Build to Spring Boot**

   Copy the contents of the `build/` folder into the backend's `resources/templates/` directory:

   ```bash
   cp -r front2/build/* src/main/resources/templates/
   ```
 
3. **Run the Spring Boot Application**

   From the project root:

   * If using **Maven**:

     ```bash
     ./mvnw spring-boot:run
     ```
  
## Contributing

Everyone is welcome to contribute by reporting bugs, suggesting features, or providing improvements.

## License & Ownership
This project is **free to use** for everyone.<br>
You are welcome to use it, modify it, and contribute.<br>
However, **redistribution, selling, or claiming this work as your own is not permitted**.<br>
All rights reserved by @Corneli F.
 
