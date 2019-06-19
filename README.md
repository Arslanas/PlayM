* [About the Project](#about-the-project)
* [Built With](#built-with)
* [Installation](#installation)
* [Usage](#usage)
* [Contact](#contact)
## About The Project
The main purpose of this application is to help in analyze of character motion for 3d animator. Key feature is easy to use managing of video frames - adding extra empty frames and removing bad looking frames, while keeping information about made changes.
It will be helpfull for animator to get correct `timing` and `spacing` - terms, that are fundamental for animation. Further improvement of animation in 3d scene, is based on information about changes that was been made.

Due to playblasts are almost never more than 15 seconds, application is supposed to be used only for short videos (playblasts).

### Demo
<div align="center">
 <a href="https://www.youtube.com/watch?v=ksk8PmWISeY"><img src="https://i.postimg.cc/4yzSHqzB/Play-M-thumbnail.jpg" alt="PlayM demo" width="560" height="315"></a>
</div>


### Built with
Application uses `Spring Boot` and `JavaFX`. 

At the current moment application has also such features as:

*    playback only every n-th frame
    
*    mirror viewport
    
*    playback range selection
    
*    zoom viewport
    
*    drag&drop video file
    
*    undo
    
    
### Usage
All commands are assigned to keyboard keys. The list of features and assigned hotkeys is possible to get by pressing `F1`. Information will be written to console.
Application is still in development, but it is already okay for using. 

For now only supported format is `mov`. I am planning to continue working on current application and in a future I will add more supported formats.

I am planning to add more important features like drawing upon video, `ghost` function on drawn frames and other...

### Installation
Archive with sample playblasts and sequence of images you can download from [here](https://drive.google.com/open?id=1ZvIx35oriZtv5wtaGvU4Hx-YVghOmUvW).
Copy folder `assets` from archive to `resources`. 
UI test are using`assets/sample` folder for searching images.

### Contact
Arslan - [ovezberdyevarslan@gmail.com]()