Vue.component("stub-param", {
    props: ["param", "index"],
    template: "#param-template"
});

Vue.component("stub", {
    props: ["stub"],
    template: "#stub-template",
    data: function(){
	return {expanded : false};
    },
    computed: {
	hasParamDesc: function(){
	    //console.log(this.stub.ret, this.stub.params);
	    if (this.stub.ret.desc)
		return true;
	    for (var i = 0; i < this.stub.params.length; i ++)
		if (this.stub.params[i].desc)
		    return true;
	    return false;
	},
	isEnum: function(){
	    return this.stub.ret.type == "enum";
	}
    },
    methods: {
	toggleExpanded: function(e){
	    this.expanded = !this.expanded;
	    e.preventDefault();
	    e.stopPropagation();
	    //console.log(this.hasParamDesc);
	}
    }
});

Vue.component("filter-set", {
    props: ["names", "by", "mode", "alter"],
    template: "#filter-set",
    data: function(){
	return {checked: [], any : !this.mode || this.mode=="any"};
    },
    methods: {
	match: function(props){
	    if (this.checked.length == 0)
		return true;
	    if (this.any){
		for (var i = 0; i < props.length; i ++){
		    if (this.checked.includes(props[i]))
			return true;
		}
		return false;
	    } else {
		for (var i = 0; i < this.checked.length; i ++){
		    if (!props.includes(this.checked[i]))
			return false;
		}
		return true;
	    }
	}
    }
});

Vue.component("searchbar", {
    template: "#searchbar",
    data: function(){
	return {withDesc: false, keyword: ""};
    },
    computed: {
	keywords: function(){
	    return this.keyword.split(" ");
	}
    },
    methods: {
	match: function(e){
	    console.log(this.keywords);
	    var str = e.name;
	    if (this.withDesc)
		for (var p in e.desc)
		    str += e.desc[p];
	    console.log(str);
	    for (var i = 0; i < this.keywords.length; i ++){
		if (str.search(this.keywords[i]) == -1)
		    return false;
	    }
	    return true;
	}

    }
});
    
var mainVue = new Vue({
    el: "#app",
    data: {
	stubs: [],
	showstubs: [],
    },
    computed: {
	cates: function(){
	    var catesMap = {};
	    for (var p in this.stubs){
		var stub = this.stubs[p];
		for (var pp in stub.cates)
		    catesMap[stub.cates[pp]] = true;
	    }
	    var cates = [];
	    for (var p in catesMap)
		cates.push(p);
	    return cates;
	},
	headers: function(){
	    var headersMap = {};
	    for (var p in this.stubs){
		var stub = this.stubs[p];
		if (stub.header)
		    headersMap[stub.header] = true;
	    }
	    var headers = [];
	    for (var p in headersMap)
		headers.push(p);
	    return headers;
	},
	types: function(){
	    var typesMap = {};
	    for (var p in this.stubs){
		var stub = this.stubs[p];
		typesMap[stub.ret.type] = true;
		for (var pp in stub.params)
		    typesMap[stub.params[pp].type] = true;
	    }
	    var types = [];
	    for (var p in typesMap){
		types.push(p);
	    }
	    return types;
	},
	tags: function(){
	    var tagsMap = {};
	    for (var p in this.stubs){
		var stub = this.stubs[p];
		for (var pp in stub.tags)
		    tagsMap[stub.tags[pp]] = true;
	    }
	    var tags = [];
	    for (var p in tagsMap)
		tags.push(p);
	    return tags;
	}
    },
    methods: {
	updateList : function(){
	    var types = function(stub){
		var ret = [stub.ret.type];
		for (var i = 0; i < stub.params.length; i ++)
		    ret.push(stub.params[i].type);
		return ret;
	    }

	    var self = this;
	    this.showstubs = this.stubs.filter(function (e){
		return self.$refs.catesflt.match(e.cates || [])
		    && self.$refs.hdrsflt.match([e.header || ""])
		    && self.$refs.typesflt.match(types(e) || [])
		    && self.$refs.tagsflt.match(e.tags || [])
		    && self.$refs.search.match(e);
	    });
	},
    }
});

var req = new XMLHttpRequest();
req.addEventListener("load", function (){
    var stubs = jsyaml.safeLoad(this.responseText);
    stubs.forEach(function (e){
	var params = e.params || [];
	var paramsnew = [];

	params.forEach(function (pm){
	    for (var p in pm){
		var spp = p.lastIndexOf(" ");
		var type = p.substr(0, spp);
		var name = p.substr(spp + 1);

		if (type.split(" ")[0] == "return"){
		    spp = p.indexOf(" ");
		    type = p.substr(0, spp);
		    name = p.substr(spp + 1);
		}

		if (p == "return"){
		    type = "return";
		    name = "";
		}
		var desc = pm[p];
		pn = {type: type, name: name, desc: desc};
		if (!pn.desc)
		    delete pn.desc;
		else
		    pn.desc = pn.desc.split("\n");
		if (type == "return"){
		    pn.type = pn.name;
		    delete pn.name;
		    e.ret = pn;
		}else
		    paramsnew.push(pn);
	    }
	});
	e.params = paramsnew;
	e.desc = e.desc.split("\n");
	e.ret = e.ret || {type: "void"};
    });
    mainVue.showstubs = stubs;
    mainVue.stubs = stubs;
});
req.open("GET", "data.yaml");
req.send();
