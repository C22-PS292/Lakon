
# This page contains what Machine Learning's team do

  

## 1. Creating Dataset for diffrent types of wayang
 We try to find dataset of diffrent types of wayang from many sources including Kaggle and many dataset repository while adding more data from internet. from here we decide to choose 14 types of wayang characters based on the availability  and the complexity of the said wayang characters data, these are the 14 wayang that we choose :
 
	
	 - Petruk
	 - Semar
	 - Arjuna
	 - Yudhistira
	 - Nakula
	 - Kresna
	 - Cepot
	 - Gatot Kaca
	 - Antasena
	 - Gareng
	 - Hanoman
	 - Bima
	 - Bagong
	 - Abimanyu

 
 ## 2. Preparing the data
After collecting the datasets, we tried to prepare the data by reducing,resizing,formatting, and deleting the datasets so the data from every single wayang are the same and have no duplicates. from this process, we decide to make each of our wayang dataset are comprises of 100 images of wayang.

  ## 3. Model Building

 1. Unzip data from kaggle
 all the data that already prepared are uploaded to kaggle website so we can use it regardless from which team the datasets are opened.
 
 2. Image Augmentation
 Because of the datasets that we collect, we need to augment our data so it can cover more conditions of the wayang when we do the classification. the image augmentation that we do are as follow :

	
	    train_datagen = ImageDataGenerator(
	        rescale=1./255,
	        shear_range=0.2,
	        rotation_range=45,
	        fill_mode='reflect',
	        zoom_range=0.2,
	        brightness_range=[0.8, 1.2],
	        width_shift_range=0.2,
	        height_shift_range=0.2,
	        horizontal_flip=True,
	        vertical_flip=True
	    )
	    
	    validation_datagen = ImageDataGenerator(rescale=1./255)
	    
	    train_generator = train_datagen.flow_from_directory(
	        directory=train_path,
	        target_size=IMAGE_SHAPE,
	        class_mode='categorical',
	        batch_size=20,
	        color_mode='rgb'
	    )
	    
	    validation_generator = validation_datagen.flow_from_directory(
	        directory=test_path,
	        target_size=IMAGE_SHAPE,
	        class_mode='categorical',
	        batch_size=20,
	        color_mode='rgb'
	    )
	    
	    types_dict = train_generator.class_indices
	    print(types_dict)

 
  3. Model Architecture
 for the model , we use transfer learning with **MobileNetV2**
 
			> #base model MobileNetV2
			from tensorflow.keras.applications import MobileNetV2
			base_model = MobileNetV2(
			    include_top=False,
			    weights='imagenet',
			    input_shape=(IMAGE_SHAPE[0], IMAGE_SHAPE[1], 3),
			    pooling='max'
			)
			base_model.trainable = True

from there we added some layers to enhance the accuracy of the model 
	
	> clear_session()
	model = Sequential([
	    base_model,
	    Dense(512, activation='relu',
	          kernel_regularizer=regularizers.l1_l2(l1=1e-5, l2=1e-4)
	    ),
	    Dropout(0.2),
	    Dense(14, activation='softmax')
	])
	model.compile(
	    optimizer=Adam(learning_rate=LEARNING_RATE),
	    loss='categorical_crossentropy',
	    metrics=['accuracy'],
	)


  4. Model Training and validation
 we train our model so it reach atleast 85% of accuracy, we retrain the model again and again until we get our desired results of 89%
 
  5. Model saving and converting to TFLite
 we use TFLite for the deplyoment so it can be use mainly on mobile, the converted model then are handled by our **CC** and **MD** team respectively
