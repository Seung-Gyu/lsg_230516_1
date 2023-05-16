<template>
    <div>
        <div v-if="editMode" style="margin-top:-30px;">
            <div style="font-size: 16px; font-weight: 700; margin-bottom:10px;">{{label}}</div>
            <div style="position:relative; margin-left: -24px; left: 50%;">
                <v-switch
                    v-bind="$attrs"
                    v-model="value"
                    hide-details
                    inset
                    @change="change"
                    style="margin-top: 0;"
                >
                </v-switch>
            </div>
        </div>
        <div v-else>
            {{label}} :  {{value}}
        </div>
    </div>
</template>
<script>
    export default {
        name: 'Boolean',
        components:{
        },
        props: {
            value:{
                type: Boolean,
                default: false
            },
            editMode: Boolean,
        },
        methods:{
            change(){
                this.$emit("input", this.value);
            }
        }
    }
</script>
