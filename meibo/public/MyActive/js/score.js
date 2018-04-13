(function (window) {

	function Score() {
		this.Container_constructor();
		this.num = times;
		this.score = 0;

		this.msg = new createjs.Text('', "24px Arial");
		this.msg.text = '剩余:' + this.num + '次 	|	 击中:' + this.score + '只';
		this.msg.color = "#641e0a";
//		this.msg.textAlign = "right";
//		this.msg.x = w >> 1;
		this.msg.x = 50;
		this.msg.y = 25;

		this.addChild(this.msg);

	}
	var p = createjs.extend(Score, createjs.Container);

	p.addScore = function (score) {
		this.score += score;
		xx = this.num;
		if (xx <= 0) {
			xx = 0;
		}
		this.msg.text = '剩余:' + xx + '次 	|	 击中:' + this.score + '只';
	};

	p.addNum = function (nums) {
		this.num -= nums;
		xx = this.num;
		if (xx <= 0) {
			xx = 0;
		}
		times = xx;
		this.msg.text = '剩余:' + xx + '次 	|	 击中:' + this.score + '只';

	};

	window.Score = createjs.promote(Score, "Container");

}(window));
