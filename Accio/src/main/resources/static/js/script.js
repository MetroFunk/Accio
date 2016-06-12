function queryAccio(){
 var myQuery = document.getElementById('value').value;

 $.ajax({
            type:"GET",
            url: "/queryAccio",
            data: {"myQuery": myQuery},
            success: function (data) {

            var res = "";
            jQuery.each(data, function(index, value){
            res += "<div>" + value + "<\div>"
            });
             document.getElementById("queries").innerHTML = res;
            },
            error: function (data) {
            }
      });
}


function queryLucene(){
 var myQuery = document.getElementById('value').value;

 $.ajax({
            type:"GET",
            url: "/queryLucene",
            data: {"myQuery": myQuery},
            success: function (data) {

            var res = "";
            jQuery.each(data, function(index, value){
            res += "<div>" + value + "<\div>"
            });
             document.getElementById("queries").innerHTML = res;
            },
            error: function (data) {
            }
      });
}