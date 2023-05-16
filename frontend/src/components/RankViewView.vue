<template>

    <v-data-table
        :headers="headers"
        :items="rankView"
        :items-per-page="5"
        class="elevation-1"
    ></v-data-table>

</template>

<script>
    const axios = require('axios').default;

    export default {
        name: 'RankViewView',
        props: {
            value: Object,
            editMode: Boolean,
            isNew: Boolean
        },
        data: () => ({
            headers: [
                { text: "id", value: "id" },
            ],
            rankView : [],
        }),
          async created() {
            var temp = await axios.get(axios.fixUrl('/rankViews'))

            temp.data._embedded.rankViews.map(obj => obj.id=obj._links.self.href.split("/")[obj._links.self.href.split("/").length - 1])

            this.rankView = temp.data._embedded.rankViews;
        },
        methods: {
        }
    }
</script>

