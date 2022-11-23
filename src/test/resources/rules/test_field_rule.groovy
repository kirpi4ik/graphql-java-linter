rule(["FIELD", "TYPE"]) {
    if (node.name ==~ /^[a-z].*/) {
        fail(parent, node, "The fieldname `${parent.name}.${node.name}` starts with uppercase.")
    }
}