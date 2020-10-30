const functions = require('firebase-functions');
const admin = require('firebase-admin');
admin.initializeApp(functions.config().firebase)

// // Create and Deploy Your First Cloud Functions
// // https://firebase.google.com/docs/functions/write-firebase-functions
//
// exports.helloWorld = functions.https.onRequest((request, response) => {
//   functions.logger.info("Hello logs!", {structuredData: true});
//   response.send("Hello from Firebase!");
// });

exports.moduleListener = functions.firestore
  .document('modules/{docId}')
  .onWrite(async (event) => { 
    
        message = {
            notification: {
                title: 'Modules Updated',
                body: 'The Current Wintec Modules have been updated!',
            },
            topic: 'moduleTopic',
        };


    let response = await admin.messaging().send(message);
    console.log(response);

  });

