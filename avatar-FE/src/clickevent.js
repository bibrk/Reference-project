
function myFunctionCaps() {
    var x = document.getElementById("caps");
    if (x.classList.contains("capselector")) {
      x.style.display = "block";
      x.classList.remove("capselector")
    } else {
        x.classList.toggle("capselector")
    }
  }

  function myFunctionCoats() {
    var x = document.getElementById("coats");
    if (x.classList.contains("coatselector")) {
      x.style.display = "block";
      x.classList.remove("coatselector")
    } else {
        x.classList.toggle("coatselector")
    }
  }

  function myFunctionPants() {
    var x = document.getElementById("pants");
    if (x.classList.contains("pantselector")) {
      x.style.display = "block";
      x.classList.remove("pantselector")
    } else {
        x.classList.toggle("pantselector")

    }
  }


  /*   function myFunctionCaps() {
    if (!document.getElementById('coatselector')){
        element.classList.add('coatselector');

        }
    }
    */