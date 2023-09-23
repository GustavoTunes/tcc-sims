// VOLTAR TOPO
function ScrollTop() {
    document.body.scrollTop = 0;
    document.documentElement.scrollTop = 0;
  }

//  CONFIRMAR SENHA
var password = document.getElementById("senha")
, confirm_password = document.getElementById("confirmar_senha");

function validatePassword(){
if(password.value != confirm_password.value) {
  confirm_password.setCustomValidity("As senhas não coincidem!");
} else {
  confirm_password.setCustomValidity('');
}
}

password.onchange = validatePassword;
confirm_password.onkeyup = validatePassword;