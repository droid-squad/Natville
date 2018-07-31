# Parks-App
The Parks App is FBU android application implemented by droid squad, a location based engaging app providing outdoor activities around the ares for potential enthusiasts.

 
Time spent: **X** hours spent in total

## User Stories

### The following **required** functionality is completed:

- [X] Solves problem of users not knowing what outdoor places to visit
- [X] Reaches outdoor enthusiasts travelers and optionally photographgers
- [X] Upon Start user is presented with Google Maps Activity - Jade
- [X] User can click icon on menu bar or tab to access personal account details - Sol
- [X] User can switch activities using an action bar or tab layout - Jadal
- [X] Create request client to make REST API to Park API - Jadal
- [X] Maps with pins marking the location of parks within user location range - Jade (To be completed by 23rd)
- [X] user can see park names on pins when clicked - Jade (To be completed by 23rd)
- [X] Scroll View for Parks nearby - Jadal (To be completed by 23rd)
- [X] List view sorted by rating - Jadal
- [X] List view sorted by distance - Jadal
- [ ] User is able to use search bar to search through available parks (To be completed by 27th)
  - [X] Search Bar UI - Jade
  - [X] Searching and presenting results (on map) -Jade
  - [X] Moving name-lat,long map to Parse server - Jadal
  - [ ] Investigate if it is possible to remove . from keyname so we can get complete set of hiking trails - Jadal
  - [X] Making requests - Jadal
  - [X] Presenting results - Jadal
- [ ] User can use swipe functionality - Sol/Jade
- [X] In search view user can click park objct to see details about that object. - Sol (To be completed by 27th)
  - [X] Create UI for Park detail view
  - [X] Hook up with recycled view
- [ ] Change App logo on the home screen - Jade
- [ ] User can log in / log out / sign up - Sol
- [ ] User can click park pins on MainActivity to see park details - Jade

#### stretch goal
- [ ] Create App theme and Logos for Title Bar - In progress (wait until meeting with Billy)

### The following **optional** features are implemented:
- [ ] User can filter parks based on specific characteristics
 - [ ] Filter by difficulty
 - [ ] Filter by star rating
 - [ ] Filter by trail condition status
 - [ ] Stretch: parse summary to get more charateristics to filter on
- [X] Visiting users can see reccomendations for parks in the area
- [ ] Find and implement a package to convert a number rating to a star rating - Jade
- [ ] Achievement implementation (Need to discuss details)

## Considerations
1. What is your product pitch?

-Problem: People don't know what kind of nature places they can visit. 

-Solution: location based engaging app providing outdoor activities around the area for potential enthusiasts

2. Who are the key stakeholders for this app?

-Key Users:  outdoor enthusiasts, travelers

-Find new parks and outdoor locations/activities near them

3. What are the core flows?

-Find locally based parks

-Details about each park, key identifiers, keeps track of where the user has been

-Screens:Login screen, home screen w/ google maps search bar, search screen, profile screen

4. What will your final demo look like?

-Login-->Home (map of area can scroll around area and click the pins)-->click pin to get a detail screen-->searchbar/screen-->list of parks matching search-->click a park for detailed screen-->use activity to switch to profile screen-->show profile features and sign out

5. What mobile features do you leverage?

-Leverage maps and swiping

6. What are your technical concerns?

-Filter searching

-Finding appropriate API or making our own server database

-Clicking on pins and getting information

## Video Walkthrough

Here's a walkthrough of implemented user stories:

<img src='http://i.imgur.com/link/to/your/gif/file.gif' title='Video Walkthrough' width='' alt='Video Walkthrough' />
<img src= 'https://scontent.xx.fbcdn.net/v/wl/t1.15752-9/38064096_142006626702297_3123553723800879104_n.jpg?_nc_cat=0&_nc_log=1&oh=672a2ac5117a21661dbe7c67f9b5c420&oe=5BC871A1'/>
<img src='https://scontent.xx.fbcdn.net/v/wl/t1.15752-9/37522993_149940615906102_7975498073872793600_n.jpg?_nc_cat=0&_nc_log=1&oh=9b943f1303bb08758c25e2c1fe0fd52a&oe=5BDDAFA9'/>
<img src='https://scontent.xx.fbcdn.net/v/wl/t1.15752-9/37403519_149940632572767_4875137748995932160_n.jpg?_nc_cat=0&_nc_log=1&oh=da788c2dd955ea4bad47be9871e57cc1&oe=5BE431ED'/>
<img src='https://scontent.xx.fbcdn.net/v/wl/t1.15752-9/37544472_149940642572766_414748997316509696_n.jpg?_nc_cat=0&_nc_log=1&oh=8025c3016ff7ef02dbf80f903a807137&oe=5BCB8176'/>
<img src='https://scontent.xx.fbcdn.net/v/wl/t1.15752-9/37602550_149940692572761_544420631282712576_n.jpg?_nc_cat=0&_nc_log=1&oh=9fdbd1b638b9cad0734ddd93c69a6ac5&oe=5BCAFE50'/>
<img src='https://scontent.xx.fbcdn.net/v/wl/t1.15752-9/37595533_149940655906098_5695346386497699840_n.jpg?_nc_cat=0&_nc_log=1&oh=ff3e6c7dc3239437c9094aa21ed2594d&oe=5BE37F67'/>

GIF created with [LiceCap](http://www.cockos.com/licecap/).

## Credits

List an 3rd party libraries, icons, graphics, or other assets you used in your app.

- 


## Notes

Describe any challenges encountered while building the app.
