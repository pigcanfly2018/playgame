// JavaScript Document

(function (window) {

	function Time() {
		this.Container_constructor();
		this.fps = Math.round(createjs.Ticker.getMeasuredFPS());
		this.addEventListener("tick", timerestFun);

		this.time = 15; //
		this.gameover = true;

		this.x = 300;
		this.y = 30;

		this.createTime();

	}
	var p = createjs.extend(Time, createjs.Container);

	p.timerest = function () {
		fps = this.fps - 1;
		this.fps = fps;
		if (fps % 60 == 0) { //一秒
			this.time -= 1;
			timetext.text = "时间: " + this.time + " s";
			this.fps = Math.round(createjs.Ticker.getMeasuredFPS());
			// console.log(this.time);
			if (this.time <= 0) {
				if (this.gameover) {
					gameOver();
					this.gameover = false;
					this.createBtn();
				}
				
			}
		}
	};

	p.createTime = function () {
		timebg = new createjs.Shape();
		timebg.graphics.beginBitmapFill(loader.getResult("TimeBg")).drawRect(0, 0, 149, 40);
		timebg.x = 40;
		timebg.y = -12;

		timetext = new createjs.Text("", "bold 22px Arial");
		timetext.color = "#660000";
		timetext.x = 55;
		timetext.y = -3;
		timetext.text = "时间: " + this.time + " s";

		this.addChild(timebg, timetext);
	};

	p.createBtn = function () {
		ExitBtn.x = 160; //返回首页的位置

		btncnt = new createjs.Container();
		btnbg = exitbg.clone();
		btntext = exittext.clone();
		btntext.text = "继续游戏"

		btncnt.x = 280;
		btncnt.y = 300;
		btncnt.scaleX=btncnt.scaleY=0.7;

		btncnt.addEventListener("pressup", goahead);
		btncnt.addChild(btnbg, btntext);
		over.addChild(btncnt);

	};

	function timerestFun() {
		time.timerest();
	}

	function goahead() {
		stage.removeChild(time, over);
		ExitBtn.x = 550;
		ExitBtn.y = 20;
		ExitBtn.regX = 0;

		stage.addEventListener("pressup", handleDown);
		score.msg.text = '剩余次数：' + score.num + '次  击中：' + score.score + '次';
		mouseshow.addEventListener("tick", mousedoTick);
		ExitBtn.removeEventListener("pressup", restart);
		stage.addEventListener("stagemousemove", handleMouseMove);
		mouseshow.addEventListener("tick", mousedoTick);

		time = new Time();
		stage.addChild(time, ExitBtn);
	}

	window.Time = createjs.promote(Time, "Container");

}(window));
