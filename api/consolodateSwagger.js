const $RefParser = require('@apidevtools/json-schema-ref-parser');
const fs = require('fs');

const BASE_DIR = './src/main/resources/';

const options = {
    continueOnError: false,
    dereference: {
        circular:true
    },
    resolve:{
        file:{
            read(file) {
                console.log('Reading ',file.url);
                return fs.readFileSync(file.url);
            }
        }
    }
}

const createFormSchema = (parentDir,filename) => {
    console.log('Creating form schema for ',parentDir,filename);
    const schemaJson = JSON.parse(fs.readFileSync(parentDir + '/' + filename).toString('utf-8'));
    var dir = './target';
    if( !fs.existsSync(dir)) {
        fs.mkdirSync(dir);
    }

    $RefParser.dereference(parentDir + '/',schemaJson,options,(err,schema)=>{
        if(err){
            console.error('Error parsing form schemas: ',err);
        }else {
            const formSchemaFileName = dir + '/' + filename;
            fs.writeFile(formSchemaFileName,JSON.stringify(schema,null,4),'ascii',(err)=>{
                if(!err){
                    console.log('Created ',formSchemaFileName );
                }else{
                    console.log('Created ',formSchemaFileName );
                }
            })
        }
    })
}

createFormSchema(BASE_DIR,'swagger.json')