## Inspiration
In August, one of our team members was hit by a drunk driver. She survived with a few cuts and bruises, but unfortunately, there are many victims who are not as lucky. The emotional and physical trauma she and other drunk-driving victims experienced motivated us to try and create a solution in the problem space.

Our team initially started brainstorming ideas to help victims of car accidents contact first response teams faster, but then we thought, what if we could find an innovative way to reduce the amount of victims? How could we help victims by preventing them from being victims in the first place, and ensuring the safety of drivers themselves? 

Despite current preventative methods, alcohol-related accidents still persist. According to the National Highway Traffic Safety Administration, in the United States, there is a death caused by motor vehicle crashes involving an alcohol-impaired driver every 50 minutes. The most common causes are rooted in failing to arrange for a designated driver, and drivers overestimating their sobriety. In order to combat these issues, we developed a hardware and software tool that can be integrated into motor vehicles. 

We took inspiration from the theme “Hack for a Night out”. While we know this theme usually means making the night out a better time in terms of fun, we thought that another aspect of nights out that could be improved is getting everyone home safe. Its no fun at all if people end up getting tickets, injured, or worse after a fun night out, and we’re hoping that our app will make getting home a safer more secure journey.

## What it does
This tool saves lives. 

It passively senses the alcohol levels in a vehicle using a gas sensor that can be embedded into a car’s wheel or seat. Using this data, it discerns whether or not the driver is fit to drive and notifies them. If they should not be driving, the app immediately connects the driver to alternative options of getting home such as Lyft, emergency contacts, and professional driving services, and sends out the driver’s location.

There are two thresholds from the sensor that are taken into account: no alcohol present and alcohol present. If there is no alcohol present, then the car functions normally. If there is alcohol present, the car immediately notifies the driver and provides the options listed above. Within the range between these two thresholds, our application uses car metrics and user data to determine whether the driver should pull over or not. In terms of user data, if the driver is under 21 based on configurations in the car such as teen mode, the app indicates that the driver should pull over. If the user is over 21, the app will notify if there is reckless driving detected, which is based on car speed, the presence of a seatbelt, and the brake pedal position.


## How we built it
Hardware Materials:
*    Arduino uno
*    Wires
*    Grove alcohol sensor
*    HC-05 bluetooth module
*    USB 2.0 b-a
*    Hand sanitizer (ethyl alcohol)

Software Materials:
*    Android studio
*    Arduino IDE
*    General Motors Info3 API
*    Lyft API
*    FireBase

## Challenges we ran into
Some of the biggest challenges we ran into involved Android Studio. Fundamentally, testing the app on an emulator limited our ability to test things, with emulator incompatibilities causing a lot of issues. Fundamental problems such as lack of bluetooth also hindered our ability to work and prevented testing of some of the core functionality. In order to test erratic driving behavior on a road, we wanted to track a driver’s ‘Yaw Rate’ and ‘Wheel Angle’, however, these parameters were not available to emulate on the Mock Vehicle simulator app. 
We also had issues picking up Android Studio for members of the team new to Android, as the software, while powerful, is not the easiest for beginners to learn. This led to a lot of time being used to spin up and just get familiar with the platform. Finally, we had several issues dealing with the hardware aspect of things, with the arduino platform being very finicky and often crashing due to various incompatible sensors, and sometimes just on its own regard. 


## Accomplishments that we're proud of
We managed to get the core technical functionality of our project working, including a working alcohol air sensor, and the ability to pull low level information about the movement of the car to make an algorithmic decision as to how the driver was driving. We were also able to wirelessly link the data from the arduino platform onto the android application. 


## What we learned
*    Learn to adapt quickly and don’t get stuck for too long

*    Always have a backup plan

## What's next for Drink+Dryve
*    Minimize hardware to create a  compact design for the alcohol sensor, built to be placed inconspicuously on the steering wheel

*    Testing on actual car to simulate real driving circumstances (under controlled conditions), to get parameter data like ‘Yaw Rate’ and ‘Wheel Angle’, test screen prompts on car display (emulator did not have this feature so we mimicked it on our phones), and connecting directly to the Bluetooth feature of the car (a separate apk would need to be side-loaded onto the car or some wi-fi connection would need to be created because the car functionality does not allow non-phone Bluetooth devices to be detected)

*    Other features: Add direct payment using service such as Plaid, facial authentication; use Docusign to share incidents with a driver’s insurance company to review any incidents of erratic/drunk-driving 

*    Our key priority is making sure the driver is no longer in a compromising position to hurt other drivers and is no longer a danger to themselves. We want to integrate more mixed mobility options, such as designated driver services such as Dryver that would allow users to have more options to get home outside of just ride share services, and we would want to include a service such as Plaid to allow for driver payment information to be transmitted securely. 
We would also like to examine a driver’s behavior over a longer period of time, and collect relevant data to develop a machine learning model that would be able to indicate if the driver is drunk driving more accurately. Prior studies have shown that logistic regression, SVM, decision trees can be utilized to report drunk driving with 80% accuracy. 
