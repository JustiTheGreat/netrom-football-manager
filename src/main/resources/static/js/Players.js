const baseURL = "http://localhost:8090";
const playersURL = baseURL + "/players";
const teamsURL = baseURL + "/teams";
const headerForSendingJson = { "Content-type": "application/json", };

document.addEventListener("DOMContentLoaded", getAllPlayers);

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
        url: playersURL + "/" + getObjectId(tr),
        onSuccessFunction: getAllPlayers,
    });
}

function getAllPlayers() {
    httpRequest({
        url: playersURL,
        requestType: "GET",
        onSuccessFunction: populateDataTable,
    });
}

function populateDataTable(responseData) {
    const requests = [];
    for (let i = 0; i < responseData.length; i++) {
        responseData[i] = {
            ...responseData[i],
            actions:
            '<button type="button" class="btn btn-primary btn-block actionButton" onclick="openEditForm(this.parentElement.parentElement)">Edit</button>' +
            '<button type="button" class="btn btn-danger btn-block actionButton" onclick="deleteRowData(this.parentElement.parentElement)">Delete</button>',
        };

        if (responseData[i].teamId) {
            const request = httpRequest({
                url: teamsURL + "/" + responseData[i].teamId,
                requestType: "GET",
                onSuccessFunction: (response) => responseData[i].teamId = response.name,
            });
            requests.push(request);
        }
    }

    $.when.apply(this, requests).done(() => {
        $("#dataTable").bootstrapTable("destroy");
        $("#dataTable").bootstrapTable({data: responseData});
    });
}

function setSelectData(selectId, data, valueFunction, defaultValue) {
    const select = document.getElementById(selectId);
    select.innerHTML = `<option value>${defaultValue}</option>`;
    for (let i = 0; i < data.length; i++) {
        const option = document.createElement("option");
        option.value = data[i].id;
        option.text = valueFunction(data[i]);
        select.appendChild(option);
    }
}

function readFormFieldsValues(id) {
    return {
        name: $("#name").val(),
        goalsScored: $("#goalsScored").val(),
        role: $("#role").val(),
        teamId: $("#teamId").val(),
    };
}

function setFormFieldsValues(data) {
    $("#name").val(data.name ? data.name : "");
    $("#goalsScored").val(data.goalsScored ? data.goalsScored : 0);
    $("#role").val(data.role ? data.role : "DEFENDER");
    $("#teamId").val(data.teamId);
}

function dataValidation(data) {
    return !data.name ? "Please complete all necessary fields!\n" : ""
        + data.goalsScored < 0 || !Number.isInteger(Number(data.goalsScored)) ? "Please select a sound number of goals!\n" : "";
}

function openCreateForm() {
    document.getElementById("formTitle").textContent = "Create player";

    httpRequest({
        requestType: "GET",
        url: teamsURL,
        onSuccessFunction: (responseData) => setSelectData("teamId", responseData, obj => obj.name, "No team"),
    });

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
            url: playersURL,
            headers: headerForSendingJson,
            json: JSON.stringify(data),
            onSuccessFunction: getAllPlayers,
        });
        $("#form").modal("hide");
    }
    $("#form").modal("show");
}

function openEditForm(tr) {
    document.getElementById("formTitle").textContent = "Edit player";

    httpRequest({
        requestType: "GET",
        url: teamsURL,
        onSuccessFunction: (responseData) => setSelectData("teamId", responseData, obj => obj.name, "No team"),
    });

    const id = getObjectId(tr);
    httpRequest({
        requestType: "GET",
        url: playersURL + "/" + id,
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
            url: playersURL + "/" + id,
            headers: headerForSendingJson,
            json: JSON.stringify(data),
            onSuccessFunction: getAllPlayers,
        });
        $("#form").modal("hide");
    }
    $("#form").modal("show");
}