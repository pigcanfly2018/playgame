(function (window) {

	function Mouseshow() {
		this.Container_constructor();
		this.fps = Math.round(createjs.Ticker.getMeasuredFPS());
		this.addEventListener("tick", mousedoTick);
		this.movearr = [];
		this.time = 0;
		this.fps_mun = 0;
	}
	var p = createjs.extend(Mouseshow, createjs.Container);

	p.mousedo = function () {
		fps = this.fps - 1;
		this.fps = fps;
		
		if (fps % 60 == 0) {
			this.fps_mun += 1;
			this.fps = Math.round(createjs.Ticker.getMeasuredFPS());
		}
		
		if (this.fps_mun >=1 ) {
			this.fps_mun = 0;
			var x = Math.round(Math.random() * (mouseAll.length - 1));
			if (!mouseArr[x].down) {
				mouseAll[x].mouseAnimate.gotoAndPlay('start');

				this.movearr[x] = createjs.Tween.get(mouseAll[x].mouseAnimate, {
						loop: false,
						override: true
					})
					.to({
						y: 0
					}, 200, createjs.linear)
					.wait(100)
					.call(smile, [x])
					.wait(500)
					.to({
						y: 199
					}, 200, createjs.linear)
					.call(end, [x]);
				mouseArr[x].down = true;
			} else {
				this.movearr[x] = createjs.Tween.get(mouseAll[x].mouseAnimate)
			}

			// console.log(x)
		}

	};

	function smile(x) {
		mouseAll[x].mouseAnimate.gotoAndPlay('smail');
	}

	function end(x) {
		mouseArr[x].down = false;
		mouseAll[x].downbtn = true;
	}

	window.Mouseshow = createjs.promote(Mouseshow, "Container");

}(window));
