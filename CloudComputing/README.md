# This page contain what Cloud Computing's team do

## 1.	Create Project and Setting The Billing
Go to google cloud console and make a project, choose billing account, and basic stuff. After choosing the project has been created before.

## 2.	Create Budget Alerts
Open Billing page at google cloud console then choose Budgets & alerts menu. Create Budget and fill the form such as Name, Project, Target amount, Set alert threshold rules, and Manage notifications.

## 3. Make database on Firestore

**1.	Go to Firestore from navigation menu**

On Data panel, we choose native mode, location and then create.

After that, we start make a collection. For the collection ID we choose our project name, which is lakon. For the each of document ID we use the data name from our dataset (wayang-bagong, wayang-arjuna, wayang-bima, etc).

We make for each document 4 field and fill the value based on our research from dataset:
- name
- avatar (Wayang Image)
- description
- reference (link to other website)

**2.	Setting Up the API**

For setting up the API, we go from console.firebase.google.com

Create project first, the firebase console automatically detect the project on our GCP.

After setting up, check the terms. For Paying section we choose the “pay as you go”, then we choose Android for App we want to build.

Choose the name of the project, at the end we got file google-services.json. We gave this file to Android team for API Config, auth, etc.

The google-services.json like this:
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

After that, we go to firestore database panel on sidebar, and choose rules column. Because we dont have authentication, we set the rules to allow to read, and prohibited to write. The code should look like this:
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

## 4.	Set up Cloud Storage for Image
At google cloud console go to Cloud Storage.

By default there are two bucket created by Firestore. Choose the bucket without 'staging' name then create new folder “Wayang Images” for uploaded Wayang images data to be accessible online.

For Permissions Images Go to console.firebase.google.com, choose the project and go to Build > Storage in sidebar
After that go to Rules column.

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
Then publish it. Upload wayang images then copy the link of each wayang to store in avatar field at firestore database.
