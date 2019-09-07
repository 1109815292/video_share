var motai=document.getElementById('mo')
var moimg=document.getElementById("moimg")
var realimg=document.getElementById("real")
var caption=document.getElementById("caption")

realimg.onclick=function(){
    motai.style.display="block"
    moimg.src=this.src
    caption.innerHTML=this.alt
}

var span=document.getElementById("close");

span.onclick=function(){
    motai.style.display="none";
}