# MCDownload
Small project I made for myself, feel free to use for whatever.
# API's used
https://github.com/Maximvdw/SpigotSite-API  
https://github.com/Maximvdw/SpigotSite   

# Installing
Drag and drop into plugins folder, start server, stop server, set up config.yml for desired spigotmc.org account. For more information on the 2FA key, see the explination by MaximVDW at (https://www.spigotmc.org/resources/spigotbuyernotification.34509/).
Works from 1.8 - 1.12

# commands
/download <id> <true/false> [filename]  
  - Id = id for the plugin to download from spigotmc.org  
  - true/false = should the plugin be enabled without additional restart (not recommended in most cases)  
  - filename = what should the file in the /root/plugins folder be called (no need to add extension)  

/findpl <name>  
  - W.I.P  

# permissions
mcdownload.download
  - gives permissions for /download and /findpl
