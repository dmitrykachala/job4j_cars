let ad = 1;

function post() {
    let form = document.querySelector('.formForSend');
    let validateBtn = form.querySelector('.validateBtn');
    let description = form.querySelector("#description");
    let fields = form.querySelectorAll(".field");

    form.addEventListener('submit', function (event) {
        event.preventDefault();

        let errors = form.querySelectorAll(".error");
        let err = false;

        for (let i = 0; i < errors.length; i++) {
            errors[i].remove();
        }

        for (let i = 0; i < fields.length; i++) {
            if (!fields[i].value) {
                err = true;
                let error = document.createElement("div");
                error.className = "error";
                error.style.color = "red";
                error.innerHTML = "Поле не может быть пустым";
                form[i].parentElement.insertBefore(error, fields[i]);
            }
        }
        if (!err) {
            console.log(description.value);
            form.submit();
            init();
        }
    })
}

function change(id) {

    $.ajax({
        type: "POST",
        url: "change",
        data: { ad : id}
    }).done(function() {

        init();
    });
}

function init() {

    let check = document.querySelector('#hideDone');
    $.ajax({
        type: 'GET',
        url: 'ad',
        dataType: 'json'
    }).done(function (data) {

        var table = document.getElementById("table");
        var cIds = document.getElementById('cIds');
        var cCars = document.getElementById('cCars');
        var rowCount = table.rows.length;
        var user = data.user.name;

        console.log(user);

        $('#cCars option').remove();
        for (var car of data.cars) {
            $('#cCars').append(`<option value="${car.id}">${car.name}</option>`);
        }

        $('#cIds option').remove();
        for (var category of data.categories) {
            $('#cIds').append(`<option value="${category.id}">${category.name}</option>`);
        }

        for (var i=1; i < rowCount; i++) {
            table.deleteRow(1);
        }

        for (var ad of data.ads) {

            var name = ad.user.name;
            var categories = "";

            for (var category of ad.categories) {
                categories += category.name + " ";
            }

            if (check.checked) {

                if (ad.sold) {

                    $('#table tr:last').after(`
                        <tr value="${ad.id}" hidden>
                            <td><img src="download?name=${ad.pictureLink}" width=100 height=100></td>
                            <td>${ad.car.name}</td>
                            <td>${ad.car.engine.id}</td>
                            <td>${ad.car.body}</td>
                            <td>
                                ${ad.description}
                            </td>
                            <td>${categories}</td>
                            <td>${ad.created}</td>
                            <td>${name}</td>
                            <td>
                                <input class="form-check-input done" type="checkbox" name="ad" value="${ad.id}" 
                                onclick="change(${ad.id})" checked disabled/>
                            </td>
                        </tr>`);
                } else {

                    if (user == ad.user.name) {

                        $('#table tr:last').after(`
                        <tr value="${ad.id}">
                            <td><img src="download?name=${ad.pictureLink}" width=100 height=100></td>
                            <td>${ad.car.name}</td>
                            <td>${ad.car.engine.id}</td>
                            <td>${ad.car.body}</td>
                            <td>
                                ${ad.description}
                            </td>
                            <td>${categories}</td>
                            <td>${ad.created}</td>
                            <td>${name}</td>
                            <td><input class="form-check-input done" type="checkbox" name="ad" value="${ad.id}" 
                            onclick="change(${ad.id})"/></td>
                        </tr>`);
                    } else {
                        $('#table tr:last').after(`
                        <tr value="${ad.id}">
                            <td><img src="download?name=${ad.pictureLink}" width=100 height=100></td>
                            <td>${ad.car.name}</td>
                            <td>${ad.car.engine.id}</td>
                            <td>${ad.car.body}</td>
                            <td>
                                ${ad.description}
                            </td>
                            <td>${categories}</td>
                            <td>${ad.created}</td>
                            <td>${name}</td>
                            <td><input class="form-check-input done" type="checkbox" name="ad" value="${ad.id}" 
                            onclick="change(${ad.id})" disabled/></td>
                        </tr>`);
                    }
                }
            } else {

                if (ad.sold) {

                    $('#table tr:last').after(`
                        <tr value="${ad.id}">
                            <td><img src="download?name=${ad.pictureLink}" width=100 height=100></td>
                            <td>${ad.car.name}</td>
                            <td>${ad.car.engine.id}</td>
                            <td>${ad.car.body}</td>
                            <td>
                                ${ad.description}
                            </td>
                            <td>${categories}</td>
                            <td>${ad.created}</td>
                            <td>${name}</td>
                            <td>
                                <input class="form-check-input done" type="checkbox" name="ad" value="${ad.id}" 
                                onclick="change(${ad.id})" checked disabled/>
                            </td>
                        </tr>`);
                } else {

                    if (user == ad.user.name) {

                        $('#table tr:last').after(`
                        <tr value="${ad.id}">
                            <td><img src="download?name=${ad.pictureLink}" width=100 height=100></td>
                            <td>${ad.car.name}</td>
                            <td>${ad.car.engine.id}</td>
                            <td>${ad.car.body}</td>
                            <td>
                                ${ad.description}
                            </td>
                            <td>${categories}</td>
                            <td>${ad.created}</td>
                            <td>${name}</td>
                            <td><input class="form-check-input done" type="checkbox" name="ad" value="${ad.id}" 
                            onclick="change(${ad.id})"/></td>
                        </tr>`);
                    } else {
                        $('#table tr:last').after(`
                        <tr value="${ad.id}">
                            <td><img src="download?name=${ad.pictureLink}" width=100 height=100></td>
                            <td>${ad.car.name}</td>
                            <td>${ad.car.engine.id}</td>
                            <td>${ad.car.body}</td>
                            <td>
                                ${ad.description}
                            </td>
                            <td>${categories}</td>
                            <td>${ad.created}</td>
                            <td>${name}</td>
                            <td><input class="form-check-input done" type="checkbox" name="ad" value="${ad.id}" 
                            onclick="change(${ad.id})" disabled/></td>
                        </tr>`);
                    }
                }
            }
        }

    }).fail(function (err) {
        $('#hello').text("Smth wrong");
    });
}

window.onload = function() {
    $(document).ready(init());
};
