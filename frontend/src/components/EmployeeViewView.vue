<template>

    <v-data-table
        :headers="headers"
        :items="employeeView"
        :items-per-page="5"
        class="elevation-1"
    ></v-data-table>

</template>

<script>
    const axios = require('axios').default;

    export default {
        name: 'EmployeeViewView',
        props: {
            value: Object,
            editMode: Boolean,
            isNew: Boolean
        },
        data: () => ({
            headers: [
                { text: "id", value: "id" },
            ],
            employeeView : [],
        }),
          async created() {
            var temp = await axios.get(axios.fixUrl('/employeeViews'))

            temp.data._embedded.employeeViews.map(obj => obj.id=obj._links.self.href.split("/")[obj._links.self.href.split("/").length - 1])

            this.employeeView = temp.data._embedded.employeeViews;
        },
        methods: {
        }
    }
</script>

