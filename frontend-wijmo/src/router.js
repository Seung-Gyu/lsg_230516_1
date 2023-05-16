
import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router);


import EmployeeManager from "./grid/EmployeeGrid"
import EmployeeDetail from "./components/listers/EmployeeDetail"
import RankManager from "./grid/RankGrid"
import RankDetail from "./components/listers/RankDetail"
import DepartmentManager from "./grid/DepartmentGrid"
import DepartmentDetail from "./components/listers/DepartmentDetail"


export default new Router({
    // mode: 'history',
    base: process.env.BASE_URL,
    routes: [
            {
                path: '/employees',
                name: 'EmployeeManager',
                component: EmployeeManager
            },
            {
                path: '/employees/:id',
                name: 'EmployeeDetail',
                component: EmployeeDetail
            },
            {
                path: '/ranks',
                name: 'RankManager',
                component: RankManager
            },
            {
                path: '/ranks/:id',
                name: 'RankDetail',
                component: RankDetail
            },
            {
                path: '/departments',
                name: 'DepartmentManager',
                component: DepartmentManager
            },
            {
                path: '/departments/:id',
                name: 'DepartmentDetail',
                component: DepartmentDetail
            },



    ]
})
