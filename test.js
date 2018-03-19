function test() {
   intervalId =
    setInterval(function (){
      i++;
      console.log(i, interval);
      if (i >= 11)
      clearInterval(intervalId)
    }, interval)
}



var count = 1;
    interval = 1000;
    interval_id = null;
    i = 0;


test();
setTimeout(() => {
  interval = 100
  clearInterval(intervalId);
  test();
}, 3000)
