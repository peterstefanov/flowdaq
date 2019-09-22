# flowdaq
flowdaq

### Build Frontend 
Building the frontend will compile the code and place it under the ```webui/dist```. Every time the frontend is modified must be build as well. 
Building the backend app (using maven) it will pickup the code from ```webui/dist``` and place it under '''public''' sub folder.
```bash
# Navigate to PROJECT_FOLDER/webui 
npm install
# build the project (this will put the files under dist folder)
ng build --prod --aot=true
```

### Build Backend 
```bash
# Maven Build : Execute from under the root folder, where pom.xml is present 
mvn clean install 