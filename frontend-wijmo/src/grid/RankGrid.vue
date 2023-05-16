<template>
    <div style="max-height:80vh;">
        <div class="gs-bundle-of-buttons" style="max-height:10vh;">
            <v-btn @click="addNewRow" small>
                <v-icon small>mdi-plus-circle-outline</v-icon>등록
            </v-btn>
            <v-btn  @click="editSelectedRow" small>
                <v-icon small>mdi-pencil</v-icon>수정
            </v-btn>
            <v-btn @click="deleteSelectedRows" small>
                <v-icon small>mdi-minus-circle-outline</v-icon>삭제
            </v-btn>
            <excel-export-button :exportService="this.exportService" :getFlex="getFlex" />
        </div>
        <RankViewQuery @search="search"></RankViewQuery>

        <!-- the grid -->
        <wj-flex-grid
            ref="flexGrid"
            :autoGenerateColumns="false"
            :allowAddNew="false"
            :allowDelete="true"
            :allowPinning="'SingleColumn'"
            :newRowAtTop="false"
            :showMarquee="true"
            :selectionMode="'MultiRange'"
            :validateEdits="false"
            :itemsSource="values"
            :initialized="flexInitialized"
            :selectionChanged="onSelectionChanged"
            style="margin-top:10px; max-height:65vh;"
        >
            <wj-flex-grid-filter :filterColumns="['name',]" />
            <wj-flex-grid-column binding="index" header="Number" width="2*" :isReadOnly="true" align="center" />
            <wj-flex-grid-column binding="name" header="name" width="2*" :isReadOnly="true" align="center" />
        </wj-flex-grid>
        <v-col style="margin-bottom:40px;">
            <div class="text-center">
                <v-dialog
                    v-model="openDialog"
                    width="332.5"
                    fullscreen
                    hide-overlay
                    transition="dialog-bottom-transition"
                >
                    <Rank :offline="offline"
                        class="video-card"
                        :isNew="!selectedItem"
                        :editMode="true"
                        v-model="rankModel"
                        @add="append"
                    />
                    <v-btn
                        style="position:absolute; top:2%; right:2%"
                        @click="closeDialog()"
                        depressed
                        icon 
                        absolute
                    >
                        <v-icon>mdi-close</v-icon>
                    </v-btn>
                </v-dialog>
            </div>
        </v-col>
    </div>
</template>

<script>
import RankViewQuery from '../components/RankViewQuery.vue';
import Rank from '../components/Rank.vue'

const axios = require('axios').default;

import '@grapecity/wijmo.touch';
import '@grapecity/wijmo.vue2.grid';
import '@grapecity/wijmo.vue2.grid.grouppanel';
import '@grapecity/wijmo.vue2.grid.filter'
import '@grapecity/wijmo.vue2.input';
import * as wjCore from '@grapecity/wijmo';
import { DataMap } from '@grapecity/wijmo.grid';
import { DataService } from "./data";
import { ExportService } from "./export";
import * as wjcCore from "@grapecity/wijmo";

export default {
    name : 'rank-grid',
    mixins:[Rank],
    components:{
        RankViewQuery,
        Rank,
    },
    data: () => ({
        tick : true,
        openDialog : false,
        selectedItem: null,
        selectedGrid: null,
    }),
    computed: {
        rankModel: {
            get() {
                return this.selectedItem || this.newValue;
            },
            set(value) {
                if (this.selectedItem) {
                    this.selectedItem = value;
                } else {
                    this.newValue = value;
                }
            }
        }
    },
    created(){
        this.init()
        this.dataService = new DataService();
        this.exportService = new ExportService();
        // // initializes data maps
        // this._productMap = this._buildDataMap(this.dataService.getProducts());
        // this._countryMap = new DataMap(this.dataService.getCountries(), 'id', 'name');
        // this._colorMap = this._buildDataMap(this.dataService.getColors());
        // // initializes cell templates
        // this.historyCellTemplate = CellMaker.makeSparkline({
        //     markers: SparklineMarkers.High | SparklineMarkers.Low,
        //     maxPoints: 25,
        //     label: 'price history',
        // });
        // this.ratingCellTemplate = CellMaker.makeRating({
        //     range: [1, 5],
        //     label: 'rating'
        // });
    }, 
    beforeDestroy() {
        this.exportService.cancelExcelExport();
    },
    methods:{
        flexInitialized(flexGrid) {
            this.$refs.flexGrid = flexGrid;
            let sd = new wjcCore.SortDescription("country", true);
            flexGrid.collectionView.sortDescriptions.push(sd);
            flexGrid.collectionView.currentChanged.addHandler(
                this._updateCurrentInfo.bind(this)
            );
            this._updateCurrentInfo();
        },
        onSelectionChanged(s) {
            let selectedItem = s.collectionView.currentItem;
            if (selectedItem) {
                this.selectedGrid = selectedItem;
                this.selectedGrid = this.selectedGrid.map((user, index) => {
                    return { ...user, index: index };
                });

            } else {
                this.selectedGrid = [];
            }
            this.$nextTick(() => {
                this.$refs.flexGridDetails.refresh();
            });
        },
        addNewRow() {
            this.selectedItem = null;
            this.openDialog = true;
        },
        editSelectedRow() {
            const flexGrid = this.$refs.flexGrid;
            const view = flexGrid.collectionView;

            if (view.currentItem) {
                this.selectedItem = null;
                this.selectedItem = JSON.parse(JSON.stringify(view.currentItem));
                this.edit(this.selectedItem);
                this.openDialog = true;
            }
        },
        async deleteSelectedRows() {
            try {
                if (!this.offline) {
                    const flexGrid = this.$refs.flexGrid;
                    const view = flexGrid.collectionView;
                    
                    if (view.currentItem) {
                        const deleteUrl = view.currentItem._links.rank.href;
                        await axios.delete(axios.fixUrl(deleteUrl));
                        
                        view.remove(view.currentItem);
                        this.values = view.sourceCollection;
                    }
                }
            } catch(e) {
                this.snackbar.status = true
                if(e.response && e.response.data.message) {
                    this.snackbar.text = e.response.data.message
                } else {
                    this.snackbar.text = e
                }
            }
        },
        groupPanelInitialized: function (ctl) {
            this.groupPanel = ctl;
            if (this.flex) {
                this.groupPanel.grid = this.flex;
            }
        },
        exportToPdf: function() {
            this.exportService.exportToPdf(this.flex, {
                countryMap: this._countryMap,
                colorMap: this._colorMap,
                historyCellTemplate: this.historyCellTemplate
            });
        },
        _buildDataMap: function (items) {
            const map = [];
            for (let i = 0; i < items.length; i++) {
                map.push({ key: i, value: items[i] });
            }
            return new DataMap(map, 'key', 'value');
        },
        getFlex: function () {
            return this.flex;
        },
        departmentId(href){
            if(href){
                return href.split('/')[2]
            }
            return null;
        },
        getChangeCls: function (value) {
            if (wjCore.isNumber(value)) {
                if (value > 0) {
                    return 'change-up';
                }
                if (value < 0) {
                    return 'change-down';
                }
            }
            return '';
        },
    },
    filters: {
        safeCurrency: function (value) {
            if (wjCore.isNumber(value)) {
                return wjCore.Globalize.formatNumber(value, 'c');
            }
            if (!wjCore.isUndefined(value) && value !== null) {
                return wjCore.changeType(value, wjCore.DataType.String);
            }
            return '';
        }
    },
}
</script>

<style>
    @import "~@grapecity/wijmo.styles/wijmo.css";
    @import "~bootstrap/dist/css/bootstrap.css";
    @import url('https://cdnjs.cloudflare.com/ajax/libs/flag-icon-css/3.1.0/css/flag-icon.css');

    body {
        font-size:1.5em;
        font-family: -apple-system,BlinkMacSystemFont,"Segoe UI Light",Roboto,Oxygen-Sans,Ubuntu,Cantarell,"Helvetica Neue",sans-serif;
    }

    .toolbar-item {
        margin-bottom: 6px;
    }

    .wj-flexgrid .wj-cell {
        padding: 7px;
        border:none;
    }

    .wj-cell.wj-state-invalid:not(.wj-header)::after {
        top: -14px;
        border: 14px solid transparent;
        border-right-color: red;
    }

    .flag-icon {
        box-shadow: 1px 1px 4px rgba(0,0,0,0.4);
    }

    .color-tile {
        display: inline-block;
        position: relative;
        width: 1em;
        height: 1em;
        border-radius: 50%;
        box-shadow: 1px 1px 4px rgba(0,0,0,0.4);
        vertical-align: middle;
    }

    .change-up {
        color: darkgreen;
    }
    .change-up:after {
        content: '\25b2';
    }
    .change-down {
        color: darkred;
    }
    .change-down:after {
        content: '\25bc';
    }

    .cell-rating {
        font-size: 12px;
    }

    .wj-flexgrid .wj-detail {
        padding: 4px 16px;
    }

    .wj-detail h3 {
        margin:10px 0;
    }
</style>
