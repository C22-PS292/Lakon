# This page contain what Cloud Computing's team do
## A.	Setting The Billing
We go to the Billing -> Manage Billing Account -> Add Billing Account for each month we want to this application

## B.	Make Database on Firestore and Setting Up the API
Go to google console and make a project, choose billing account, and basic stuff. After choosing the project has been created before, we do:

### 1.	Go to Firestore from navigation menu
On Data panel, we choose native mode, location and then create.
After that, we start make a collection. For the collection ID we choose our project name, which is lakon. For the each of document ID we use the data name from our dataset (wayang-bagong, wayang-arjuna, wayang-bima, etc).
We make for each document 4 field and fill the value based on our research from dataset:
- name
- avatar (Wayang image)
- description
- reference link (to access other website and another description)

### 2.	Setting Up the API
For setting up the API, we go from console.firebase.google.com
We create project, the firebase console automatically detect the project on our GCP
After setting up, check-check the terms. For paying we choose the “pay as you go”, then we choose Android for App we want to build.
Choose the name of the project, and at the end, we got file google-services.json. We gave this file to Android team for API Config, auth, etc.

The google-services.json like this
```bash
{
  "project_info": {
    "project_number": "791700040851",
    "project_id": "lakon-capstone",
    "storage_bucket": "lakon-capstone.appspot.com"
  },
  "client": [
    {
      "client_info": {
        "mobilesdk_app_id": "1:791700040851:android:4126002205ae0e86493166",
        "android_client_info": {
          "package_name": "com.lakonstudio.lakon"
        }
      },
      "oauth_client": [
        {
          "client_id": "791700040851-n6vnrflqidtjhu57jtsddph3963l2itr.apps.googleusercontent.com",
          "client_type": 3
        }
      ],
      "api_key": [
        {
          "current_key": "AIzaSyAudeA3O7JBbRDbrTtssbQnv4ZtfMLBhmY"
        }
      ],
      "services": {
        "appinvite_service": {
          "other_platform_oauth_client": [
            {
              "client_id": "791700040851-n6vnrflqidtjhu57jtsddph3963l2itr.apps.googleusercontent.com",
              "client_type": 3
            }
          ]
        }
      }
    }
  ],
  "configuration_version": "1"
}
```

After that, we go to firestore database panel on sidebar, and choose rules column. Because we dont have authentication, we set the rules to allow to read, and prohibited to write. The code should look like this
```bash
rules_version = '2';
service cloud.firestore {
  match /databases/{database}/documents {
    match /{document=**} {
      allow write: if false;
      allow read: if true;
    }
  }
}
```
Then publish it

## C.	Make Storage for Image
Go to google console and choose the project, then go to Cloud storage from navigation menu
Choose the one without 'staging' name
Make the folder “Name” for saving the uploaded taken image from user
For Permissions Images Go to console.firebase.google.com, choose the project and go to Build > Storage in sidebar
After that go to Rules column

Here, we changed the permissions to access data (both read and write without Authentication), so the code looks like this:
```bash
rules_version = '2';
service firebase.storage {
  match /b/{bucket}/o {
    match /{allPaths=**} {
      // allow read, write: if request.auth != null;
      allow read, write: if true;
    }
  }
}
```
Then publish it. Now users can upload image to the storage

