document.addEventListener('DOMContentLoaded', function() {
    const registrarseBtn = document.getElementById('registrarse');
    registrarseBtn.disabled = true;

    function verificarUsername() {
        const username = document.getElementById('username').value;
        const mensaje = document.getElementById('mensaje');

        if (usernames.includes(username)) {
            mensaje.textContent = 'Error: El nombre de usuario ya existe.';
            mensaje.style.color = 'red';
            return false;
        } else {
            if (mensaje.textContent === 'Error: El nombre de usuario ya existe.') {
                mensaje.textContent = '';
            }
            return true;
        }
    }

    function verificarPassword() {
        const password = document.getElementById('password').value;
        const confPassword = document.getElementById('confPassword').value;
        const mensaje = document.getElementById('mensaje');

        if (password !== confPassword) {
            if (mensaje.textContent === 'Error: El nombre de usuario ya existe.') {
                mensaje.textContent = 'Error: Las contraseñas no coinciden.';
                mensaje.style.color = 'blue';
            } else
            return false;
        } else {
            if (mensaje.textContent === 'Error: Las contraseñas no coinciden.') {
                mensaje.textContent = '';
            }
            return true;
        }
    }

    function validarFormulario() {
        const isUsernameValid = verificarUsername();
        const isPasswordValid = verificarPassword();

        registrarseBtn.disabled = !(isUsernameValid && isPasswordValid);
    }

    document.getElementById('username').addEventListener('input', function() {
        verificarUsername();
        validarFormulario();
    });
    document.getElementById('password').addEventListener('input', function() {
        verificarPassword();
        validarFormulario();
    });
    document.getElementById('confPassword').addEventListener('input', function() {
        verificarPassword();
        validarFormulario();
    });
});