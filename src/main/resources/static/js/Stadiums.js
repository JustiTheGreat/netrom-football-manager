const baseURL = "http://localhost:8090";
const stadiumsURL = baseURL + "/stadiums";
const headerForSendingJson = { "Content-type": "application/json", };

document.addEventListener("DOMContentLoaded", getAllStadiums);

function httpRequest(obj) {
    return $.ajax({
        type: obj.requestType,
        url: obj.url,
        headers: obj.headers,
        data: obj.json,
        success: obj.onSuccessFunction,
        error: (error) => console.log(`Error ${error}`),
    });
}

function getObjectId(tr) {
    return tr.getAttribute("data-uniqueid");
}

function deleteRowData(tr) {
    httpRequest({
        requestType: "DELETE",
        url: stadiumsURL + "/" + getObjectId(tr),
        onSuccessFunction: getAllStadiums,
    });
}

function getAllStadiums() {
    httpRequest({
        url: stadiumsURL,
        requestType: "GET",
        onSuccessFunction: populateDataTable,
    });
}

function populateDataTable(responseData) {
    for (let i = 0; i < responseData.length; i++) {
        responseData[i] = {
            ...responseData[i],
            actions:
            '<button type="button" class="btn btn-primary btn-block actionButton" onclick="openEditForm(this.parentElement.parentElement)">Edit</button>' +
            '<button type="button" class="btn btn-danger btn-block actionButton" onclick="deleteRowData(this.parentElement.parentElement)">Delete</button>',
        };
    }
    $("#dataTable").bootstrapTable("destroy");
    $("#dataTable").bootstrapTable({data: responseData});
}

function readFormFieldsValues() {
    return {
        name: $("#name").val(),
        location: $("#location").val(),
    };
}

function setFormFieldsValues(data) {
    $("#name").val(data.name ? data.name : "");
    $("#location").val(data.name ? data.name : "");
}

function dataValidation(data) {
    return !data.name || !data.location ? "Please complete all necessary fields!\n" : "";
}

function openCreateForm() {
    document.getElementById("formTitle").textContent = "Create stadium";

    setFormFieldsValues({});

    const finishButton = document.getElementById("dialogFinishButton");
    finishButton.textContent = "Create";
    finishButton.onclick = () => {
        const data = readFormFieldsValues();

        if (dataValidation(data)) {
            alert(dataValidation(data));
            return;
        }

        httpRequest({
            requestType: "POST",
            url: stadiumsURL,
            headers: headerForSendingJson,
            json: JSON.stringify(data),
            onSuccessFunction: getAllStadiums,
        });
        $("#form").modal("hide");
    }
    $("#form").modal("show");
}

function openEditForm(tr) {
    document.getElementById("formTitle").textContent = "Edit stadium";

    const id = getObjectId(tr);
    httpRequest({
        requestType: "GET",
        url: stadiumsURL + "/" + id,
        onSuccessFunction: setFormFieldsValues,
    });

    const finishButton = document.getElementById("dialogFinishButton");
    finishButton.textContent = "Update";
    finishButton.onclick = () => {
        const data = readFormFieldsValues();
        data.id = id;

        if (dataValidation(data)) {
            alert(dataValidation(data));
            return;
        }

        httpRequest({
            requestType: "PUT",
            url: stadiumsURL + "/" + id,
            headers: headerForSendingJson,
            json: JSON.stringify(data),
            onSuccessFunction: getAllStadiums,
        });
        $("#form").modal("hide");
    }
    $("#form").modal("show");
}