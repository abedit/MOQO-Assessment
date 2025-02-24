# MOQO Assessment
This project serves to store the assessment for the Junior Mobile Developer position at MOQO. The prompt is to load a list of Points of Interest (POI) and display them on a map. Upon clicking on a marker, to show more details about the POI that was clicked.

Brief summary of the app:
- Loads a maximum of 10 POIs from the given API. If returned result is exactly 10, load the next page of results.
- Show POIs as markers on the map.
- Once a marker is clicked, show a card on the bottom that the user can click.
- Once the card is clicked, load more details about the POI and show it in a sheet on top of the map.

 
Notes:
- UI wise, I took inspiration from the current MOQO app on the [playstore](https://play.google.com/store/apps/details?id=de.moqo.work). 
- The "Loading pill" on the bottom of the map screen displays the number of the currently displayed POIs on the map.
- Since a lot of the POIs have the same latitude and longitude, they are grouped to show 1 marker instead of multiple markers in the exact same position. A marker with grouped data will show a badge on the top right to inform the user that this marker hosts more than 1 POI.
- Upon clicking on a marker, a card will be displayed on the bottom with quick information about the POI. If the clicked marker has more than 1 POI grouped, the right amount of POI cards will be shown as a horizontal list.
- Once a POI card is clicked, a sheet will be displayed with detailed information about the POI.
- In the email received with the challenge, I was told to only use the native SDK. Therefore, only native Android SDKs were used in the project.

