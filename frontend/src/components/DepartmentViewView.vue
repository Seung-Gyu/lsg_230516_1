<template>

    <v-data-table
        :headers="headers"
        :items="departmentView"
        :items-per-page="5"
        class="elevation-1"
    ></v-data-table>

</template>

<script>
    const axios = require('axios').default;

    export default {
        name: 'DepartmentViewView',
        props: {
            value: Object,
            editMode: Boolean,
            isNew: Boolean
        },
        data: () => ({
            headers: [
                { text: "id", value: "id" },
            ],
            departmentView : [],
        }),
          async created() {
            var temp = await axios.get(axios.fixUrl('/departmentViews'))

            temp.data._embedded.departmentViews.map(obj => obj.id=obj._links.self.href.split("/")[obj._links.self.href.split("/").length - 1])

            this.departmentView = temp.data._embedded.departmentViews;
        },
        methods: {
        }
    }
</script>

