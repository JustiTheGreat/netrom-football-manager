const baseURL = "http://localhost:8090";
const teamsURL = baseURL + "/teams";
const headerForSendingJson = { "Content-type": "application/json", };

document.addEventListener("DOMContentLoaded", getAllTeams);

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
        url: teamsURL + "/" + getObjectId(tr),
        onSuccessFunction: getAllTeams,
    });
}

function getAllTeams() {
    httpRequest({
        url: teamsURL,
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

function readFormFieldsValues(id) {
    return {
        name: $("#name").val(),
    };
}

function setFormFieldsValues(data) {
    $("#name").val(data.name ? data.name : "");
}

function dataValidation(data) {
    return !data.name ? "Please complete all necessary fields!\n" : "";
}

function openCreateForm() {
    document.getElementById("formTitle").textContent = "Create team";

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
            url: teamsURL,
            headers: headerForSendingJson,
            json: JSON.stringify(data),
            onSuccessFunction: getAllTeams,
        });
        $("#form").modal("hide");
    }
    $("#form").modal("show");
}

function openEditForm(tr) {
    document.getElementById("formTitle").textContent = "Edit team";

    const id = getObjectId(tr);
    httpRequest({
        requestType: "GET",
        url: teamsURL + "/" + id,
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
            url: teamsURL + "/" + id,
            headers: headerForSendingJson,
            json: JSON.stringify(data),
            onSuccessFunction: getAllTeams,
        });
        $("#form").modal("hide");
    }
    $("#form").modal("show");
}