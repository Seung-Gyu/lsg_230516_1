<template>
    <div></div>
</template>

<script>
    import RankBase from '../components/RankBase.vue'
    import DepartmentBase from '../components/DepartmentBase.vue'
    const axios = require('axios').default;

    export default{
        name: 'employee-base',
        props: {
            offline: Boolean
        },
        computed: {},
        data: () => ({
            values: [],
            newValue: {},
            isUpdating: false,
        }),
        methods:{
            closeDialog(){
                this.openDialog = false
                location.reload()
            },
            append(value){
                this.tick = false
                this.newValue = {}
                this.values.push(value)
                this.$emit('input', this.values);
                this.$nextTick(function(){
                    this.tick=true
                })
            },
            remove(value){
                var where = -1;
                for(var i=0; i<this.values.length; i++){
                    if(this.values[i]._links.self.href == value._links.self.href){
                        where = i;
                        break;
                    }
                }
                if(where > -1){
                    this.values.splice(i, 1);
                    this.$emit('input', this.values);
                }
            },
            async getRealEntity(id){
               try{
                    let result = await axios.get(axios.fixUrl(`/employees/${id}`))
                    result.data.id = id;
                    return result.data;
                }catch(e){
                    return null;
                }
            },
            async processData(data){
                let RankClass = this.$Vue.extend(RankBase);
                this.rankId = new RankClass();
                let DepartmentClass = this.$Vue.extend(DepartmentBase);
                this.departmentId = new DepartmentClass();
                

                let Promises = data.map(async (value) => {
                    if(value == null) return
                    if (value.rankId && value.rankId.id){
                        value.rankId = await this.rankId.getRealEntity(value.rankId.id);
                    }
                    if (value.departmentId && value.departmentId.id){
                        value.departmentId = await this.departmentId.getRealEntity(value.departmentId.id);
                    }
                });
                await Promise.all(Promises);
                for(var i = 0; i < data.length ; i++ ) {
                    data[i].index = i;
                }
                return data;

            },
            async search(query) {
                var me = this;
                if(me.offline){
                    if(!me.values) me.values = [];
                    return;
                } 
                var temp = null;
                if(query!=null){
                    temp = await axios.put(axios.fixUrl('/employees/search/findByUserId'), { userId: query.parameters.userId });
                    me.values = await me.processData(temp.data);
                }else{
                    temp = await axios.get(axios.fixUrl('/employees'))
                    me.values = await me.processData(temp.data._embedded.employees);
                }
                return me.values;
            },
        },

    }

</script>
