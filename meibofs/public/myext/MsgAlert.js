/**
 * html5 audio play
 */
var MsgAlert={
		audio:undefined,
		canPlay:false,
		init:function(){
			this.audio=document.createElement("audio");
			this.audio.src = "/public/myext/mp3/msg.mp3";
			this.audio.loop = false;
			this.audio.addEventListener("canplaythrough", function () {
				MsgAlert.canPlay=true;
				
			}, false);
		},
		play:function(){
			this.audio.play();
		},
		replay:function(){
			try{
			this.audio.currentTime = 0;
			this.audio.play();
			}catch(e){}
		},
		pause:function(){
			this.audio.pause();
		}
}


        	

        