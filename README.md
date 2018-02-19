# ScienceStories_Gameified
***The StoryLab at Texas A&M Android Wear Application***

### Purpose
The purpose of this application is to create an informal science learning application on a wearable device. This program can then be modified based on the scope of the study for future potential publications. 

- - - -
### Layout
* Vocabulary
* Prompt
* Bird prior to recording
* Audio Record
* Bird after recording
* Change Prompt

- - - -
### Prompts
Prompts are used to help aid the students in science reflection focused around three steps of the scientific method.

There are three different types of prompts for this application:
* What around you may be related to [Science Topic]?  - Observation
* How can what you are doing relate to [Science Topic]? - Activity
* Do something related to [Science Topic]. Tell me about it. - Experiment

- - - -
### Gamification of the application - Change in bird according to the number of recordings in each prompt.
To make the application more engaging and intuitive, the application behaves like a game in which there is a bird that grows as more and more recordings are done. As there are three types of prompts, the bird changes in a different way for each prompt.

- - - -
### Class Initialization
The application loads initially into the MainActivity class, displaying the vocabulary of the science lesson the user is learning.
- - - -
### Recording Notes
To compensate Android Wear from being unable to record using the general Android recording class, the MessageRecord class contains code to record an audio stream into a .PCM file. This .PCM file is then given a header, translated into a .WAV file, and the .PCM is then deleted. The application then uses the .WAV file for audio playback immediately after the recording is done.

- - - -
### Other Classes

#### BirdBefore
Displays the current state of the bird.

#### BirdAfter
Displays the state of the bird after a recording has been done.

- - - -
### To run
Download this repository directly to Android Studio. Can either run via the debugger or create a .APK file to install via scripts.
