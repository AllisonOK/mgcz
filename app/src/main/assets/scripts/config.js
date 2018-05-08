var loadingView= window.loadingView;
if(loadingView)
{
    loadingView.loadingAutoClose=false;
	loadingView.showTextInfo=false;
	setTimeout(function(){
	    loadingView.loading(100);
	},2000);
    loadingView.bgColor("#ffffff");
    loadingView.setFontColor("#000000");
    loadingView.setTips(["", "", ""]);
}
window.onLayaInitError=function(e)
{
	console.log("onLayaInitError error=" + e);
	alert("加载游戏失败，可能由于您的网络不稳定，请退出重进");
}