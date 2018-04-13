(function (window) {

	function Mouse() {
		this.Container_constructor();
		this.mouse = new createjs.Bitmap(loader.getResult("mouse"));
		this.mouse.regX = this.mouse.image.width >> 1;
		this.mouse.x = 0;
		this.mouse.y = 199;
		this.down = false;
		this.downbtn = true;
		this.ing = true;

		this.mouseSpriteSheet = new createjs.SpriteSheet({ //娃娃动画
			framerate: 100,
			"images": [loader.getResult("mouseall")],
			"frames": {
				"regX": 71,
				"regY": 0,
				"width": 141,
				"height": 199,
				"count": 8
			},
			"animations": {
				"smail": [1, 3, "smail", 0.2],
				"yun": [4, 7, "yun", 0.2],
				"start": 0
			}
		});

		this.mouseAnimate = new createjs.Sprite(this.mouseSpriteSheet, "start");
		this.mouseAnimate.x = 0;
		this.mouseAnimate.y = 199;
		this.mouseAnimate.scaleX = this.mouseAnimate.scaleY = 0.7;
		// this.mouseAnimate.gotoAndPlay('smail');

		this.addChild(this.mouseAnimate);

	}
	var p = createjs.extend(Mouse, createjs.Container);

	p.mouseshow = function () {

	};

	window.Mouse = createjs.promote(Mouse, "Container");

}(window));
