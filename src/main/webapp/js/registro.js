document.addEventListener('DOMContentLoaded', function() {
    const formulario = document.getElementById('receta-form');
    const registrarseBtn = document.getElementById('registrarse');
    registrarseBtn.disabled = true;

    function validarEmail(email) {
        // Expresión regular para validar emails
        const regexEmail = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        
        // Dominios permitidos (opcionalmente restringir)
        const dominiosPermitidos = ['gmail.com', 'outlook.com', 'hotmail.com', 'yahoo.com', 'correo.com', 'unal.edu.co', 'unicolmayor.edu.co'];
        
        if (!regexEmail.test(email)) {
            return { valido: false, mensaje: 'El correo no tiene un formato válido (debe contener @ y dominio)' };
        }
        
        // Validar dominio
        const dominio = email.split('@')[1].toLowerCase();
        if (!dominiosPermitidos.includes(dominio) && !email.includes('@')) {
            return { valido: true, mensaje: 'Correo válido pero no reconocido. Úsalo bajo tu responsabilidad.' };
        }
        
        return { valido: true, mensaje: 'Correo válido' };
    }

    function verificarEmail() {
        const email = document.getElementById('correo').value;
        const mensaje = document.getElementById('mensaje');
        
        if (email === '') {
            mensaje.textContent = '';
            mensaje.classList.remove('show', 'error', 'success');
            return false;
        }
        
        const resultado = validarEmail(email);
        if (!resultado.valido) {
            mensaje.textContent = 'Error: ' + resultado.mensaje;
            mensaje.classList.add('show', 'error');
            return false;
        } else {
            mensaje.textContent = resultado.mensaje;
            mensaje.classList.add('show', 'success');
            return true;
        }
    }

    function verificarUsername() {
        const username = document.getElementById('username').value;
        const mensaje = document.getElementById('mensaje');

        if (username === '') {
            return false;
        }

        if (username.length < 3) {
            mensaje.textContent = 'Error: El nombre de usuario debe tener al menos 3 caracteres.';
            mensaje.classList.add('show', 'error');
            return false;
        }

        if (usernames.includes(username)) {
            mensaje.textContent = 'Error: El nombre de usuario ya existe.';
            mensaje.classList.add('show', 'error');
            return false;
        } else {
            mensaje.textContent = 'Nombre de usuario disponible';
            mensaje.classList.add('show', 'success');
            return true;
        }
    }

    function verificarPassword() {
        const password = document.getElementById('password').value;
        const confPassword = document.getElementById('confPassword').value;
        const mensaje = document.getElementById('mensaje');

        if (password === '' || confPassword === '') {
            return false;
        }

        if (password.length < 6) {
            mensaje.textContent = 'Error: La contraseña debe tener al menos 6 caracteres.';
            mensaje.classList.add('show', 'error');
            return false;
        }

        if (password !== confPassword) {
            mensaje.textContent = 'Error: Las contraseñas no coinciden.';
            mensaje.classList.add('show', 'error');
            return false;
        } else {
            mensaje.textContent = 'Las contraseñas coinciden';
            mensaje.classList.add('show', 'success');
            return true;
        }
    }

    function validarFormulario() {
        const nombre1 = document.getElementById('nombre_1').value.trim();
        const apellido1 = document.getElementById('apellido_1').value.trim();
        const correo = document.getElementById('correo').value.trim();
        const username = document.getElementById('username').value.trim();
        const password = document.getElementById('password').value;
        const confPassword = document.getElementById('confPassword').value;

        const emailValido = correo !== '' ? validarEmail(correo).valido : false;
        const usernameValido = username.length >= 3 && !usernames.includes(username);
        const passwordValido = password === confPassword && password.length >= 6;
        const camposRequeridos = nombre1 !== '' && apellido1 !== '' && correo !== '';

        registrarseBtn.disabled = !(emailValido && usernameValido && passwordValido && camposRequeridos);
    }

    // Event listeners para validación en tiempo real
    document.getElementById('nombre_1').addEventListener('input', validarFormulario);
    document.getElementById('apellido_1').addEventListener('input', validarFormulario);
    document.getElementById('correo').addEventListener('input', function() {
        verificarEmail();
        validarFormulario();
    });
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

    // Manejo del envío del formulario - Solo se usa para envío tradicional
    formulario.addEventListener('submit', function(e) {
        // No es necesario hacer e.preventDefault() ya que dejaremos que se envíe normalmente
        // Solo hacemos validaciones finales
        const nombre1 = document.getElementById('nombre_1').value.trim();
        const apellido1 = document.getElementById('apellido_1').value.trim();
        const correo = document.getElementById('correo').value.trim();
        const username = document.getElementById('username').value.trim();
        const password = document.getElementById('password').value;
        const confPassword = document.getElementById('confPassword').value;

        if (!nombre1 || !apellido1 || !correo || !username || !password || !confPassword) {
            e.preventDefault();
            const mensaje = document.getElementById('mensaje');
            mensaje.textContent = 'Por favor, completa todos los campos requeridos';
            mensaje.classList.add('show', 'error');
            return false;
        }

        if (password !== confPassword) {
            e.preventDefault();
            const mensaje = document.getElementById('mensaje');
            mensaje.textContent = 'Las contraseñas no coinciden';
            mensaje.classList.add('show', 'error');
            return false;
        }

        if (!validarEmail(correo).valido) {
            e.preventDefault();
            const mensaje = document.getElementById('mensaje');
            mensaje.textContent = 'Correo inválido';
            mensaje.classList.add('show', 'error');
            return false;
        }

        if (usernames.includes(username)) {
            e.preventDefault();
            const mensaje = document.getElementById('mensaje');
            mensaje.textContent = 'El nombre de usuario ya existe';
            mensaje.classList.add('show', 'error');
            return false;
        }
    });
});

function togglePasswordVisibility(id) {
    var passwordField = document.getElementById(id);
    var toggleIcon = passwordField.nextElementSibling;
    if (passwordField.type === "password") {
        passwordField.type = "text";
        toggleIcon.classList.remove("fa-eye");
        toggleIcon.classList.add("fa-eye-slash");
    } else {
        passwordField.type = "password";
        toggleIcon.classList.remove("fa-eye-slash");
        toggleIcon.classList.add("fa-eye");
    }
}