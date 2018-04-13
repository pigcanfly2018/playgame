(function (window) {
	function TextLink(text, font, color) {
		
		this.Text_constructor(text, font, color);
		this.hover = false;
		this.hitArea = new createjs.Shape();
		this.textBaseline = "top";
	}
	var p = createjs.extend(TextLink, createjs.Text);

	TextLink.prototype.draw = function (ctx, ignoreCache) {
		var color = this.color;
		
		this.Text_draw(ctx, ignoreCache);

		this.color = color;

		this.hitArea.graphics.clear().beginFill("#FFF").drawRect(0, 0, this.getMeasuredWidth(), this.getMeasuredHeight());
	};

	window.TextLink = createjs.promote(TextLink, "Text");
}(window));